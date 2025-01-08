package dev.priporov.ideanotes.tree.exporting

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserFactory.getInstance
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.node.ROOT_ID
import dev.priporov.ideanotes.tree.state.StateService
import dev.priporov.ideanotes.tree.state.TreeState
import dev.priporov.ideanotes.util.FileNodeUtils
import dev.priporov.ideanotes.util.WriteActionUtils
import java.io.File
import java.io.FileOutputStream
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readBytes

const val STATE_FILE_NAME = "state.json"
const val EXPORT_STATE_FILE_NAME = "exp_state.json"
private const val DEFAULT_FILE_NAME = "IdeaNotes"

class ExportService {

    private var objectMapper = ObjectMapper()
    private var stateFilePath = "${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$STATE_FILE_NAME"
    private var exportingStateFilePath = "${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$EXPORT_STATE_FILE_NAME"

    fun exportNotesToFile(tree: NoteTree, node: FileTreeNode = tree.root): () -> Unit = {
        createZipFile(tree)?.also { file ->
            exportToZip(file, node)
        }
    }

    private fun createZipFile(tree: NoteTree) = getInstance()
        .createSaveFileDialog(FileSaverDescriptor("Export", ""), tree)
        .save("${DEFAULT_FILE_NAME}_${LocalDateTime.now().toLocalDate()}.zip")
        ?.file

    private fun exportToZip(target: File, node: FileTreeNode): File {
        val sourceFolderPath = FileNodeUtils.baseDir.toPath()
        val zipPath = target.toPath()
        val zos = ZipOutputStream(FileOutputStream(zipPath.toFile()))
        val newState = TreeState()
        val map = HashMap<String, String>()
        val filesToRemove = ArrayList<VirtualFile?>()
        Files.walkFileTree(
            sourceFolderPath,
            FileVisitor(
                service<StateService>(),
                zos,
                newState,
                map,
                sourceFolderPath,
                filesToRemove,
                getFilter(getAllIds(node))
            )
        )

        fillOrder(newState, service<StateService>().getTreeState(), map, node.id)
        val stateFile = saveStateToJsonFile(newState, exportingStateFilePath)

        zos.putNextEntry(ZipEntry(sourceFolderPath.relativize(stateFile.toPath()).toString()))
        Files.copy(stateFile.toPath(), zos)
        zos.closeEntry()
        zos.close()

        WriteActionUtils.runWriteAction {
            filesToRemove.forEach { it?.delete(this) }
            Files.delete(Path.of(exportingStateFilePath))
        }

        return zipPath.toFile()
    }

    private fun getFilter(allIds: Set<String>) = { path: Path ->
        allIds.contains(path.nameWithoutExtension)
    }

    private fun getAllIds(node: FileTreeNode): HashSet<String> {
        val state = service<StateService>().getTreeState()
        val queue: Queue<String> = LinkedList<String>().apply {
            if (node.type != NodeType.SOFT_LINK) {
                add(node.id!!)
            }
        }
        val ids = HashSet<String>()
        while (queue.isNotEmpty()) {
            val id = queue.poll()
            ids.add(id)
            state.getOrderByParentId(id).also { list ->
                if (list != null) {
                    queue.addAll(list)
                }
            }
        }
        return ids
    }

    fun saveStateToJsonFile(treeState: TreeState, path:String = stateFilePath): File {
        val file = File(path).apply {
            if (!exists()) {
                createNewFile()
            }
        }
        file.writeBytes(objectMapper.writeValueAsBytes(treeState))

        return file
    }

    private fun fillOrder(
        newState: TreeState,
        oldState: TreeState,
        map: HashMap<String, String>,
        sourceId: String?
    ): TreeState {
        val softLinkKeys = oldState.getNodes().filterValues { it.type == NodeType.SOFT_LINK }.keys

        if (sourceId != ROOT_ID) {
            newState.getOrder()[ROOT_ID] = mutableListOf(map[sourceId])
        }

        for ((id, list) in oldState.getOrder()) {
            val newId = if (id == ROOT_ID && sourceId == ROOT_ID)
                ROOT_ID
            else map[id]
                ?: continue
            if (softLinkKeys.contains(id)) {
                continue
            }

            newState.getOrder().putIfAbsent(
                newId,
                list.asSequence()
                    .filterNotNull()
                    .filterNot { softLinkKeys.contains(it) }
                    .mapNotNull { map[it] }
                    .toMutableList()
            )
        }
        return newState
    }
}

class FileVisitor(
    private val stateService: StateService,
    private val zos: ZipOutputStream,
    private val newState: TreeState,
    private val map: HashMap<String, String>,
    private val sourceFolderPath: Path,
    private val filesToRemove: MutableList<VirtualFile?>,
    private val filter: (path: Path) -> Boolean = { path -> path.name != STATE_FILE_NAME ||  path.name != EXPORT_STATE_FILE_NAME }
) : SimpleFileVisitor<Path>() {

    override fun visitFile(path: Path, attrs: BasicFileAttributes?): FileVisitResult {
        if (!filter.invoke(path)) {
            return FileVisitResult.CONTINUE
        }
        val info = stateService.getTreeState().getNodes()[path.nameWithoutExtension]
        if (info?.type == NodeType.SOFT_LINK) {
            return FileVisitResult.CONTINUE
        }
        val copiedNodeWithNewId = newNodeFromFile(path, info!!.type)

        newState.saveNodeWithoutSavingState(copiedNodeWithNewId)
        map[path.nameWithoutExtension] = copiedNodeWithNewId.id!!

        val nioPath = copiedNodeWithNewId.getFile()!!.toNioPath()
        zos.putNextEntry(ZipEntry(sourceFolderPath.relativize(nioPath).toString()))
        Files.copy(path, zos)
        zos.closeEntry()

        filesToRemove.add(copiedNodeWithNewId.getFile())

        return FileVisitResult.CONTINUE
    }

    private fun newNodeFromFile(file: Path, type: NodeType?): FileTreeNode {
        val name = file.nodeId()
        val extension = file.extension

        return FileTreeNode(name, extension, type = type).apply {
            initFile()
            WriteActionUtils.runWriteAction { getFile()?.setBinaryContent(file.readBytes()) }
        }
    }
}

private fun Path.nodeId() = this.nameWithoutExtension.substringBeforeLast("_")
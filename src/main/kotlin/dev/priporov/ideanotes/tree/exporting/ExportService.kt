package dev.priporov.ideanotes.tree.exporting

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserFactory.getInstance
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.NoteTree
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
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readBytes

const val STATE_FILE_NAME = "state.json"
private const val DEFAULT_FILE_NAME = "IdeaNotes.zip"

class ExportService {

    private val stateService: StateService = service()
    private var objectMapper = ObjectMapper()
    private var stateFilePath = "${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$STATE_FILE_NAME"

    fun exportNotesToFile(tree: NoteTree, node: FileTreeNode = tree.root): () -> Unit = {
        createZipFile(tree)?.also { file ->
            exportToZip(file, node)
        }
    }

    private fun createZipFile(tree: NoteTree) = getInstance()
        .createSaveFileDialog(FileSaverDescriptor("Export", ""), tree)
        .save(DEFAULT_FILE_NAME)
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
                zos,
                newState,
                map,
                sourceFolderPath,
                filesToRemove,
                getFilter(node, getAllIds(node))
            )
        )

        fillOrder(newState, stateService.state, map, node.id)
        val stateFile = saveStateToJsonFile(newState)

        zos.putNextEntry(ZipEntry(sourceFolderPath.relativize(stateFile.toPath()).toString()))
        Files.copy(stateFile.toPath(), zos)
        zos.closeEntry()
        zos.close()

        WriteActionUtils.runWriteAction {
            filesToRemove.forEach { it?.delete(this) }
        }

        return zipPath.toFile()
    }

    private fun getFilter(node: FileTreeNode, allIds:Set<String>) = if (node.id != ROOT_ID) { path: Path ->
        allIds.contains(path.nameWithoutExtension)
    } else { _ ->
        true
    }

    private fun getAllIds(node: FileTreeNode): HashSet<String> {
        val order = stateService.state.order
        val queue: Queue<String> = LinkedList<String>().apply {
            add(node.id!!)
        }
        val ids = HashSet<String>()
        while (queue.isNotEmpty()) {
            val id = queue.poll()
            ids.add(id)
            order[id]?.also { list ->
                queue.addAll(list)
            }
        }
        return ids
    }

    private fun saveStateToJsonFile(treeState: TreeState): File {
        val file = File(stateFilePath).apply {
            if (exists()) {
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
        if (sourceId != ROOT_ID) {
            newState.order[ROOT_ID] = listOf(map[sourceId])
        }

        for ((id, list) in oldState.order) {
            val newId = if (id == ROOT_ID && sourceId == ROOT_ID)
                ROOT_ID
            else map[id]
                ?: continue

            newState.order.putIfAbsent(
                newId,
                list.mapNotNull { map[it] }
            )
        }
        return newState
    }
}

class FileVisitor(
    private val zos: ZipOutputStream,
    private val newState: TreeState,
    private val map: HashMap<String, String>,
    private val sourceFolderPath: Path,
    private val filesToRemove: MutableList<VirtualFile?>,
    private val filter: (path: Path) -> Boolean = { path -> path.name != STATE_FILE_NAME }
) : SimpleFileVisitor<Path>() {

    override fun visitFile(path: Path, attrs: BasicFileAttributes?): FileVisitResult {
        if (!filter.invoke(path)) {
            return FileVisitResult.CONTINUE
        }
        val copiedNodeWithNewId = newNodeFromFile(path)

        newState.saveNode(copiedNodeWithNewId)
        map[path.nameWithoutExtension] = copiedNodeWithNewId.id!!

        val nioPath = copiedNodeWithNewId.getFile()!!.toNioPath()
        zos.putNextEntry(ZipEntry(sourceFolderPath.relativize(nioPath).toString()))
        Files.copy(path, zos)
        zos.closeEntry()

        filesToRemove.add(copiedNodeWithNewId.getFile())

        return FileVisitResult.CONTINUE
    }

    private fun newNodeFromFile(file: Path): FileTreeNode {
        val name = file.nodeId()
        val extension = file.extension

        return FileTreeNode(name, extension).apply {
            initFile()
            WriteActionUtils.runWriteAction { getFile()?.setBinaryContent(file.readBytes()) }
        }
    }
}

private fun Path.nodeId() = this.nameWithoutExtension.substringBeforeLast("_")
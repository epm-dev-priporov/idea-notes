package dev.priporov.ideanotes.tree.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserFactory.getInstance
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.factory.NoteNodeFactory
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.StateNodeDto
import dev.priporov.ideanotes.tree.node.rootName
import dev.priporov.ideanotes.util.WriteActionUtil
import java.io.File
import java.io.FileOutputStream
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class ExportService {

    private var objectMapper = ObjectMapper()

    fun exportNotesToFile(tree: BaseTree<*>, node: NoteNode = tree.getRoot()) {
        createZipFile(tree)?.also { file ->
            exportToZip(tree, file, node)
        }
    }

    private fun createZipFile(tree: BaseTree<*>): File? {
        return getInstance()
            .createSaveFileDialog(FileSaverDescriptor("Export", ""), tree)
            .save("asd_${LocalDateTime.now().toLocalDate()}.zip")
            ?.file
    }

    private fun exportToZip(tree: BaseTree<*>, target: File, node: NoteNode): File {
        val zipPath = target.toPath()
        val zos = ZipOutputStream(FileOutputStream(zipPath.toFile()))
        val map = HashMap<String, String>()

        val newState = generateNewState(node, map)
        val newStatePath = "${tree.getStateDirectory()}$fileSeparator$exportStateFileName"
        saveStateToJsonFile(newState, newStatePath)

        val copiedFilePaths = copyFiles(map, tree.getStateDirectory(), tree.getTreeState())

        Files.walkFileTree(
            Paths.get(tree.getStateDirectory()),
            FileVisitor(copiedFilePaths, zos)
        )
        zos.close()
        removeFiles(copiedFilePaths)

        return zipPath.toFile()
    }

    fun removeFiles(paths: Set<Path>) {
        paths.forEach { WriteActionUtil.runWriteAction { Files.delete(it) } }
    }

    private fun copyFiles(map: MutableMap<String, String>, path: String, state: TreeStateDto): Set<Path> {
        val paths = HashSet<Path>()

        state.nodesGroupedById.values.forEach { stateNodeDto ->
            val newId = map[stateNodeDto.id]
            if (newId != null) {
                WriteActionUtil.runWriteAction {
                    Files.copy(
                        Paths.get("$path$fileSeparator${stateNodeDto.id}.${stateNodeDto.type?.extension}"),
                        Paths.get("$path$fileSeparator${newId}.${stateNodeDto.type?.extension}")
                    ).apply {
                        paths.add(this)
                    }
                }
            }
        }

        return paths
    }

    private fun generateNewState(
        rootNode: NoteNode,
        map: MutableMap<String, String>
    ): TreeStateDto {
        val newState = TreeStateDto()
        val stateMap = HashMap<String, StateNodeDto>()

        val nodes = rootNode.getChildren().apply { add(rootNode) }

        nodes.forEach { node ->
            val generateNodeId = if (node.name != rootName) {
                service<NoteNodeFactory>().generateNodeId(node.name)!!
            } else {
                node.name!!
            }

            val nodeStateDto = StateNodeDto().apply {
                this.id = generateNodeId
                this.name = node.name
                this.type = node.type
            }
            map.put(node.id!!, generateNodeId)
            stateMap[generateNodeId] = nodeStateDto
        }

        nodes.forEach { node ->
            val id = node.id

            val childState = stateMap[map[id]]
            if (node.parent != null) {
                val parentId = (node.parent as NoteNode).id
                newState.insertInto(childState!!, map[parentId])
            }
        }


        return newState
    }

    private fun saveStateToJsonFile(treeState: TreeStateDto, path:String): File {
        val file = File(path).apply {
            if (!exists()) {
                createNewFile()
            }
        }
        file.writeBytes(objectMapper.writeValueAsBytes(treeState))

        return file
    }

}

class FileVisitor(private val paths: Set<Path>,private val zos: ZipOutputStream) : SimpleFileVisitor<Path>() {

    override fun visitFile(path: Path, attrs: BasicFileAttributes?): FileVisitResult {
        if (!paths.contains(path) && path.toFile().name != exportStateFileName) {
            return FileVisitResult.CONTINUE
        }
        zos.putNextEntry(ZipEntry(path.fileName.toString()))
        Files.copy(path, zos)
        zos.closeEntry()

        return FileVisitResult.CONTINUE
    }

}

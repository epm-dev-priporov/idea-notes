package dev.priporov.ideanotes.tree.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserFactory.getInstance
import com.intellij.openapi.vfs.LocalFileSystem
import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.AppNoteTree
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.ProjectNoteTree
import dev.priporov.ideanotes.tree.container.NoteNodeContainer
import dev.priporov.ideanotes.tree.container.VirtualFileContainer
import dev.priporov.ideanotes.tree.factory.NoteNodeFactory
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.NodeType
import dev.priporov.ideanotes.util.WriteActionUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Service
class ImportService {

    private val objectMapper = ObjectMapper()

    fun importNotesFromFile(tree: BaseTree<*>) {
        val zipFile = chooseZipFile(tree) ?: return

        val tempDir = Files.createTempDirectory("idea-notes-import-").toFile()
        try {
            unzip(zipFile, tempDir)

            val stateFile = File(tempDir, exportStateFileName)
            if (!stateFile.exists()) {
                return
            }

            val importedState: TreeStateDto = objectMapper.readValue(stateFile, TreeStateDto::class.java)

            val baseDir = tree.getStateDirectory()
            val copiedFiles = copyImportedFiles(importedState, tempDir, baseDir)

            restoreTreeFromState(importedState, tree, copiedFiles)
        } finally {
            deleteRecursively(tempDir)
        }
    }

    private fun chooseZipFile(tree: BaseTree<*>): File? {
        return getInstance()
            .createOpenFileDialog(null, tree)
            .openFiles
            .firstOrNull()
            ?.let { File(it.path) }
            ?.takeIf { it.extension.equals("zip", ignoreCase = true) }
    }

    private fun unzip(zipFile: File, destDir: File) {
        ZipInputStream(FileInputStream(zipFile)).use { zis ->
            var entry: ZipEntry?
            while (zis.nextEntry.also { entry = it } != null) {
                val currentEntry = entry!!
                val destFile = File(destDir, currentEntry.name)

                if (currentEntry.isDirectory) {
                    destFile.mkdirs()
                } else {
                    destFile.parentFile?.mkdirs()
                    FileOutputStream(destFile).use { fos ->
                        zis.copyTo(fos)
                    }
                }
                zis.closeEntry()
            }
        }
    }

    private fun copyImportedFiles(
        state: TreeStateDto,
        sourceDir: File,
        targetDir: String
    ): Map<String, Path> {
        val copiedFiles = HashMap<String, Path>()

        state.nodesGroupedById.values.forEach { stateNodeDto ->
            val sourceFile = File(sourceDir, "${stateNodeDto.id}.${stateNodeDto.type?.extension}")
            if (sourceFile.exists()) {
                val targetFileName = "${stateNodeDto.id}.${stateNodeDto.type?.extension}"
                val targetPath = File(targetDir, targetFileName).toPath()
                WriteActionUtil.runWriteAction {
                    Files.copy(
                        sourceFile.toPath(),
                        targetPath,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    )
                }
                copiedFiles[stateNodeDto.id!!] = targetPath
            }
        }

        return copiedFiles
    }

    private fun restoreTreeFromState(
        state: TreeStateDto,
        tree: BaseTree<*>,
        copiedFiles: Map<String, Path>
    ) {
        val noteNodeContainer = service<NoteNodeContainer>()
        val virtualFileContainer = service<VirtualFileContainer>()
        val noteNodeFactoryService = service<NoteNodeFactory>()

        val root = tree.getRoot()

        val nodesGroupedById = HashMap<String, NoteNode>()

        state.nodesGroupedById.values.forEach { stateNodeDto ->
            val node = noteNodeFactoryService.getNode(stateNodeDto.name!!, stateNodeDto.id!!).apply {
                type = stateNodeDto.type ?: NodeType.UNKNOWN
            }

            if (stateNodeDto.type != NodeType.PACKAGE) {
                val copiedPath = copiedFiles[stateNodeDto.id!!]
                if (copiedPath != null) {
                    val localFileSystem = LocalFileSystem.getInstance()
                    val virtualFile = localFileSystem.refreshAndFindFileByNioFile(copiedPath)
                        ?: localFileSystem.refreshAndFindFileByPath(copiedPath.toString())
                    if (virtualFile != null) {
                        node.file = virtualFile
                        virtualFileContainer.put(stateNodeDto.id!!, virtualFile)
                    }
                }
            }

            nodesGroupedById[stateNodeDto.id!!] = node
        }

        state.hierarchy.forEach { (parentId, childIds) ->
            val parentNode = if (parentId == rootName) {
                root
            } else {
                nodesGroupedById[parentId]
            }

            if (parentNode != null) {
                childIds.forEach { childId ->
                    val childNode = nodesGroupedById[childId]
                    if (childNode != null) {
                        parentNode.insert(childNode, parentNode.childCount)
                    }
                }
            }
        }

        tree.getTreeModel().reload(root)
        tree.expandAll()

        val treeStateService = when (tree) {
            is AppNoteTree -> service<ApplicationTreeStateService>()
            is ProjectNoteTree -> tree.project.getService(ProjectTreeStateService::class.java)
            else -> null
        }

        treeStateService?.treeState = state
        treeStateService?.saveStateFile(state)
    }

    private fun deleteRecursively(file: File) {
        if (file.isDirectory) {
            file.listFiles()?.forEach { deleteRecursively(it) }
        }
        file.delete()
    }
}

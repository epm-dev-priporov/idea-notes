package dev.priporov.ideanotes.tree.importing

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.templates.github.ZipUtil
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.dto.NodeStateInfo
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper
import dev.priporov.ideanotes.tree.exporting.EXPORT_STATE_FILE_NAME
import dev.priporov.ideanotes.tree.exporting.STATE_FILE_NAME
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.node.ROOT_ID
import dev.priporov.ideanotes.tree.state.TreeInitializer
import dev.priporov.ideanotes.tree.state.TreeState
import dev.priporov.ideanotes.util.FileNodeUtils
import dev.priporov.ideanotes.util.WriteActionUtils
import java.io.File
import java.nio.file.Files

private const val ZIP = "zip"

class ImportService {

    private val treeInitializer = service<TreeInitializer>()
    private val objectMapper = ObjectMapper()

    fun import(tree: NoteTree): () -> Unit = {
        val descriptor = fileChooserDescriptor()

        FileChooser.chooseFile(descriptor, null, null)?.also { virtualFile: VirtualFile ->
            val file = virtualFile.toNioPath().toFile()
            when {
                ZIP == file.extension -> importFromZipFile(file, tree)
                ExtensionFileHelper.containsExtension(file.extension) -> importFile(file, tree)
            }
        }
    }

    fun importFromJsonState(tree: NoteTree) {
        val file = File("${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$STATE_FILE_NAME")
        if (!file.exists()) {
            file.createNewFile()
        } else {
            val bytes = file.readBytes()
            if (bytes.size != 0) {
                var state: TreeState = objectMapper.readValue(bytes, TreeState::class.java)
                treeInitializer.initTreeModelFromState(state, tree)
            }
        }
    }

    private fun importZippedFromJsonState(tree: NoteTree) {
        val baseZipDir = "${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$ZIP"
        val importedState = importJsonAndRemoveExpFile(baseZipDir)?.run {
            regenerateIds(this, baseZipDir)
        }

        val dirForImport = File(baseZipDir)
        FileNodeUtils.removeDir(dirForImport)

        if (importedState != null) {
            treeInitializer.initTreeFromImportedState(importedState, tree)
        }
    }

    private fun importJsonAndRemoveExpFile(baseZipDir: String): TreeState? {
        val stateJson = File("$baseZipDir${FileNodeUtils.fileSeparator}$STATE_FILE_NAME")
        val expStateFileJson = File("$baseZipDir${FileNodeUtils.fileSeparator}$EXPORT_STATE_FILE_NAME")
        if (stateJson.exists() && !expStateFileJson.exists()) {
            stateJson.renameTo(expStateFileJson)
        }

        val treeState = objectMapper.readValue(expStateFileJson.readBytes(), TreeState::class.java)
        if(stateJson.exists()){
            Files.delete(stateJson.toPath())
        }
        if(expStateFileJson.exists()){
            Files.delete(expStateFileJson.toPath())
        }

        return treeState
    }

    private fun importFile(file: File, tree: NoteTree) {
        val extension = file.extension
        val name = file.nameWithoutExtension
        val type = ExtensionFileHelper.EXTENSIONS.values.find { it.extension == extension }?.type
        if (type != null) {
            val node = tree.insert(NodeCreationInfo(tree.root, name, extension, type))
            FileNodeUtils.copyToNode(file, node)
        }
    }

    private fun importFromZipFile(file: File, tree: NoteTree) {
        unzipFile(file)
        importZippedFromJsonState(tree)
    }

    private fun regenerateIds(state: TreeState, baseZipDir: String): TreeState {
        val mapOfIds = HashMap<String, String>()
        val newState = TreeState()
        for ((oldId, nodeInfo) in state.getNodes()) {
            val node: FileTreeNode = newNode(nodeInfo)
            newState.saveNodeWithoutSavingState(node)

            mapOfIds[oldId] = node.id!!
            val bytes = File("$baseZipDir${FileNodeUtils.fileSeparator}${nodeInfo.id}.${nodeInfo.extension}").readBytes()
            node.setData(bytes)
        }

        val childIds: MutableList<String?>? = state.getOrder()[ROOT_ID]
        childIds
            ?.map { mapOfIds[it] }
            ?.toMutableList()
            ?.also { newChildIds -> newState.getOrder()[ROOT_ID] = newChildIds }

        for ((oldId, childrenIds) in state.getOrder()) {
            val newId = mapOfIds[oldId]
            if (oldId != ROOT_ID) {
                newState.getOrder()[newId] = childrenIds.map { mapOfIds[it] }.toMutableList()
            }
        }

        return newState
    }

    private fun newNode(nodeInfo: NodeStateInfo): FileTreeNode {
        val node = FileTreeNode(nodeInfo)
        WriteActionUtils.runWriteAction {
            node.generateNewId()
        }

        return node
    }

    private fun unzipFile(file: File) {
        val unzipDir = File("${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$ZIP")
        if (!unzipDir.exists()) {
            unzipDir.mkdir()
        }

        ZipUtil.unzip(
            null,
            unzipDir,
            file,
            null,
            null,
            true
        )
    }

    private fun fileChooserDescriptor() = FileChooserDescriptor(
        true,
        false,
        true,
        true,
        false,
        false
    )

}
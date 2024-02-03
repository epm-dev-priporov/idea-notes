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
import dev.priporov.ideanotes.tree.exporting.ExportService
import dev.priporov.ideanotes.tree.exporting.STATE_FILE_NAME
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.node.ROOT_ID
import dev.priporov.ideanotes.tree.state.ReaderState
import dev.priporov.ideanotes.tree.state.TreeInitializer
import dev.priporov.ideanotes.tree.state.TreeState
import dev.priporov.ideanotes.util.FileNodeUtils
import dev.priporov.ideanotes.util.WriteActionUtils
import java.io.File

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

    fun importOldNotes(oldState: ReaderState){
        if ( oldState.isImported == true) {
            return
        }
        val file = File("${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$STATE_FILE_NAME")
        if (!file.exists()) {
            file.createNewFile()
        }
        val bytes = file.readBytes()
        var state = if (bytes.size != 0) {
            objectMapper.readValue(bytes, TreeState::class.java)
        } else {
            TreeState()
        }
        oldState.order[ROOT_ID].also {
            val elements = state.getOrderByParentId(ROOT_ID)
            if (elements != null) {
                it?.addAll(elements)
            }
        }
        state.addNodes(oldState.nodes)
        state.addOrder(oldState.order)
        service<ExportService>().saveStateToJsonFile(state)
        oldState.isImported = true
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
        val bytes = File("${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$STATE_FILE_NAME").readBytes()
        var state: TreeState = objectMapper.readValue(bytes, TreeState::class.java)
        state = regenerateIds(state)
        treeInitializer.initTreeModelFromState(state, tree)
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

    private fun regenerateIds(oldState: TreeState): TreeState {
        val mapOfIds = HashMap<String, String>()
        val newState = TreeState()
        for ((oldId, nodeInfo) in oldState.getNodes()) {
            val node = newNode(nodeInfo)
            newState.saveNode(node)
            mapOfIds[oldId] = node.id!!
        }

        val childIds = oldState.getOrder()[ROOT_ID]
        childIds?.map { mapOfIds[it] }?.also { list ->
            newState.getOrder()[ROOT_ID] = list
        }

        for ((oldId, childrenIds) in oldState.getOrder()) {
            val newId = mapOfIds[oldId]
            if (oldId != ROOT_ID) {
                newState.getOrder()[newId] = childrenIds.map { mapOfIds[it] }
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
        ZipUtil.unzip(
            null,
            FileNodeUtils.baseDir,
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
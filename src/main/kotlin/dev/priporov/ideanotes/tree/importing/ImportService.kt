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
import dev.priporov.ideanotes.tree.exporting.STATE_FILE_NAME
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.node.ROOT_ID
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

    private fun importFile(file: File, tree: NoteTree){
        val extension = file.extension
        val name = file.nameWithoutExtension

        val node = tree.insert(NodeCreationInfo(tree.root, name, extension))
        FileNodeUtils.copyToNode(file, node)
    }

    private fun importFromZipFile(file: File, tree: NoteTree) {
        unzipFile(file)
        val bytes = File("${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$STATE_FILE_NAME").readBytes()
        var state: TreeState = objectMapper.readValue(bytes, TreeState::class.java)
        state = regenerateIds(state)
        treeInitializer.initTreeModelFromState(state, tree)
    }

    private fun regenerateIds(oldState: TreeState): TreeState {
        val map = HashMap<String, String>()
        val newState = TreeState()
        for ((oldId, nodeInfo) in oldState.nodes) {
            val node = newNode(nodeInfo)
            newState.saveNode(node)
            map[oldId] = node.id!!
        }

        val childIds = oldState.order[ROOT_ID]
        childIds?.map { map[it] }?.also { list ->
            newState.order[ROOT_ID] = list
        }

        for ((oldId, childrenIds) in oldState.order) {
            val newId = map[oldId]
            newState.order[newId] = childrenIds.map { map[it] }
        }
        return newState
    }

    private fun newNode(
        nodeInfo: NodeStateInfo
    ): FileTreeNode {
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
package dev.priporov.ideanotes.tree.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper
import dev.priporov.ideanotes.tree.common.VirtualFileContainer
import dev.priporov.ideanotes.tree.importing.ImportService
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.TreeModelProvider

@State(
    name = "StateService",
    storages = [Storage("ideanotes.xml")]
)
class StateService : PersistentStateComponent<ReaderState> {
    private val treeModelProvider = service<TreeModelProvider>()
    private val virtualFileContainer = service<VirtualFileContainer>()
    private val treeInitializer = service<TreeInitializer>()
    private var state = TreeState()
    private var readerState = ReaderState()

    fun updateOrder(parent: FileTreeNode) = state.saveOrder(parent)

    fun saveNodeInfo(node: FileTreeNode) {
        state.saveNode(node)
        virtualFileContainer.addNode(node)
    }

    fun removeNodeInfo(node: FileTreeNode) {
        node.getFile()?.also { file -> virtualFileContainer.removeFile(file) }
        state.removeNodeInfo(node)
    }

    fun getNodesByParentId(id:String) = state.getOrderByParentId(id)

    fun getTreeState(): TreeState {
        return state
    }

    override fun getState(): ReaderState {
        return readerState
    }

    override fun loadState(loadedState: ReaderState) {
        readerState = loadedState
    }

    fun contains(id: String, parentId: String): Boolean {
        return state.getOrder()[parentId]?.contains(id) ?: false
    }
}
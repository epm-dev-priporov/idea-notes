package dev.priporov.ideanotes.tree.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.common.VirtualFileContainer
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.TreeModelProvider

@State(
    name = "StateService",
    storages = [Storage("ideanotes.xml")]
)
class StateService : PersistentStateComponent<TreeState> {
    private val treeModelProvider = service<TreeModelProvider>()
    private val virtualFileContainer = service<VirtualFileContainer>()
    private val treeInitializer = service<TreeInitializer>()
    private var state = TreeState()

    fun updateOrder(parent: FileTreeNode) = state.saveOrder(parent)

    fun saveNodeInfo(node: FileTreeNode) {
        state.saveNode(node)
        node.getFile()?.also { file -> virtualFileContainer.addFile(file) }
    }

    fun removeNodeInfo(node: FileTreeNode) {
        node.getFile()?.also { file -> virtualFileContainer.removeFile(file) }
        state.removeNodeInfo(node)
    }

    override fun getState(): TreeState {
        return state
    }

    override fun loadState(loadedState: TreeState) {
        state = loadedState
        val defaultTreeModel = treeModelProvider.getModel()
        if (defaultTreeModel == null) {
            treeModelProvider.setCallBack {
                treeInitializer.initTreeModelFromState(state, treeModelProvider.getModel())
            }
        } else {
            treeInitializer.initTreeModelFromState(state, defaultTreeModel)
        }
    }

    fun contains(id: String, parentId: String): Boolean {
        return state.order[parentId]?.contains(id) ?: false
    }
}
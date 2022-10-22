package dev.priporov.ideanotes.tree.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.common.VirtualFileContainer
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.TreeModelProvider
import javax.swing.tree.DefaultTreeModel

@State(
    name = "StateService",
    storages = [Storage("ideanotes.xml")]
)
class StateService : PersistentStateComponent<TreeState> {
    private val treeModelProvider = service<TreeModelProvider>()
    private val virtualFileContainer = service<VirtualFileContainer>()
    private var state = TreeState()

    fun updateOrder(parent: FileTreeNode) = state.saveOrder(parent)

    fun saveNodeInfo(node: FileTreeNode) {
        state.saveNodeInfo(node)
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
            treeModelProvider.setCallBack { loadNodes(treeModelProvider.getModel()) }
        } else {
            loadNodes(defaultTreeModel)
        }
    }

    private fun loadNodes(model: DefaultTreeModel?) {
        if (model == null) {
            error("model can not be null")
        }
        val root = model.root as FileTreeNode
        val map = state.nodes
        val createdNodes: MutableMap<String?, FileTreeNode> = map.values
            .asSequence()
            .map { FileTreeNode(it) }
            .associateByTo(HashMap()) { it.id }

        createdNodes[root.id] = root

        state.order.forEach { (key, list) ->
            val parent = createdNodes[key]
            parent?.getFile()?.also { file -> virtualFileContainer.addFile(file) }
            list.forEach {
                val newNode = createdNodes[it]
                parent?.insert(newNode, parent.childCount)
                newNode?.getFile()?.also { file -> virtualFileContainer.addFile(file) }
            }
        }

        model.reload()
    }

}
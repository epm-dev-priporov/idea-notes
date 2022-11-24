package dev.priporov.ideanotes.tree.state

import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.VirtualFileContainer
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.util.*
import javax.swing.tree.DefaultTreeModel

class TreeInitializer {

    private val virtualFileContainer = service<VirtualFileContainer>()

    fun initTreeModelFromState(state: TreeState, tree: NoteTree) {
        val stateService = service<StateService>()
        val model = tree.getDefaultTreeModel()
        val root = model.root as FileTreeNode

        val createdNodes: MutableMap<String?, FileTreeNode> = state.nodes.values
            .asSequence()
            .map { FileTreeNode(it) }
            .associateByTo(HashMap()) { it.id }

        createdNodes[root.id] = root
        val queue: Queue<String> = LinkedList<String>().apply { add(root.id!!) }

        while (queue.isNotEmpty()) {
            val parentId = queue.poll()
            val parent = createdNodes[parentId] ?: continue
            parent.getFile()?.also { file -> virtualFileContainer.addFile(file) }
            state.order[parentId]?.also { list ->
                queue.addAll(list)
                for (id in list) {
                    if (stateService.contains(id!!, parentId)) {
                        continue
                    }
                    val newNode = createdNodes[id] ?: continue
                    tree.insert(parent, newNode)

                    newNode.getFile()?.also { file -> virtualFileContainer.addFile(file) }
                }
            }
        }

        model.reload()
    }

    fun initTreeModelFromState(state: TreeState, model: DefaultTreeModel?) {
        if (model == null) {
            error("model can not be null")
        }
        val root = model.root as FileTreeNode

        val createdNodes: MutableMap<String?, FileTreeNode> = state.nodes.values
            .asSequence()
            .map { FileTreeNode(it) }
            .associateByTo(HashMap()) { it.id }

        createdNodes[root.id] = root
        val queue = LinkedList<String?>().apply { add(root.id) }

        while (queue.isNotEmpty()) {
            val id = queue.poll()
            val elements = state.order[id]
            if (elements.isNullOrEmpty()) {
                continue
            }
            queue.addAll(elements)

            val parent = createdNodes[id]
            parent?.getFile()?.also { file -> virtualFileContainer.addFile(file) }
            for (childId in elements) {
                val newNode = createdNodes[childId]
                if (newNode != null) {
                    parent?.insert(newNode, parent.childCount)
                    newNode.getFile()?.also { file -> virtualFileContainer.addFile(file) }
                }
            }
        }

        model.reload()
    }

}
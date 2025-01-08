package dev.priporov.ideanotes.tree.state

import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.VirtualFileContainer
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.util.*

class TreeInitializer {

    private val virtualFileContainer = service<VirtualFileContainer>()

    fun initTreeModelFromState(state: TreeState, tree: NoteTree) {
        val model = tree.getDefaultTreeModel()
        val root = model.root as FileTreeNode

        val createdNodes: MutableMap<String?, FileTreeNode> = createAndGroupNodesById(state)

        createdNodes[root.id] = root
        val queue: Queue<String> = LinkedList<String>().apply { add(root.id!!) }

        while (queue.isNotEmpty()) {
            val parentId = queue.poll()
            val parent = createdNodes[parentId] ?: continue
            virtualFileContainer.addNode(parent)
            virtualFileContainer.init(parent.getFile())

            state.getOrder()[parentId]?.also { list ->
                queue.addAll(list)
                for (id in list) {
                    val newNode = createdNodes[id] ?: continue
                    tree.insert(parent, newNode)

                    virtualFileContainer.addNode(newNode)
                    virtualFileContainer.init(newNode.getFile())
                }
            }
        }

        model.reload()
    }

    fun initTreeFromImportedState(importedState: TreeState, tree: NoteTree) {

        val stateService = service<StateService>()
        val model = tree.getDefaultTreeModel()
        val root = model.root as FileTreeNode

        val createdNodes: MutableMap<String?, FileTreeNode> = createAndGroupNodesById(importedState)

        createdNodes[root.id] = root
        val queue: Queue<String> = LinkedList<String>().apply { add(root.id!!) }

        while (queue.isNotEmpty()) {
            val parentId = queue.poll()
            val parent = createdNodes[parentId] ?: continue
            virtualFileContainer.addNode(parent)
            virtualFileContainer.init(parent.getFile())

            importedState.getOrder()[parentId]?.also { list ->
                queue.addAll(list)
                for (id in list) {
                    if (stateService.contains(id!!, parentId)) {
                        continue
                    }
                    val newNode = createdNodes[id] ?: continue
                    tree.insert(parent, newNode)

                    virtualFileContainer.addNode(newNode)
                    virtualFileContainer.init(newNode.getFile())
                }
            }
        }

        model.reload()
    }

    private fun createAndGroupNodesById(state: TreeState): MutableMap<String?, FileTreeNode> {
        return state.getNodes().values
            .asSequence()
            .map { FileTreeNode(it) }
            .associateByTo(HashMap()) { it.id }
    }

}
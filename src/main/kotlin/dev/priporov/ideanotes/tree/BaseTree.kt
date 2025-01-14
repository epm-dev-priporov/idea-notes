package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.service
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto
import dev.priporov.ideanotes.tree.node.mapper.CreateDtoToTreeNodeMapper
import java.util.*
import javax.swing.tree.DefaultTreeModel

abstract class BaseTree<T : DefaultTreeModel> : Tree() {

    private val nodesGroupedById = HashMap<String, NoteNode>()

    open fun createNewInRoot(createNodeDto: CreateNodeDto): NoteNode {
        val root = getRoot()

        val node: NoteNode = service<CreateDtoToTreeNodeMapper>().toFileTreeNode(createNodeDto)

        root.insert(
            node,
            root.childCount
        )

        getTreeModel().reload(root)

        nodesGroupedById[node.id!!] = node

        return node
    }

    fun expandAll() {
        val list = LinkedList<NoteNode>()
        val root = model.root as NoteNode

        list.add(root)

        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isCollapsed(TreeUtil.getPath(root, treeNode))) {
                expandPath(TreeUtil.getPath(root, treeNode))
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as NoteNode })
        }
    }

    fun collapseAll() {
        val root = getRoot()

        getExpandedNodes(root).asSequence().filter { it != root }.forEach {
            collapsePath(TreeUtil.getPath(it.parent, it))
        }
    }

    fun getTreeModel() = model as T

    fun getSelectedNode(): NoteNode? = selectionPath?.lastPathComponent as? NoteNode

    fun openInEditor(node: NoteNode) {
        println("openInEditor: $node")
    }

    private fun getRoot() = getTreeModel().root as NoteNode

    private fun getExpandedNodes(node: NoteNode): ArrayList<NoteNode> {
        val list = LinkedList<NoteNode>()
        val expandedNodes = ArrayList<NoteNode>()
        val root = getRoot()

        list.add(node)
        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isExpanded(TreeUtil.getPath(root, treeNode))) {
                expandedNodes.add(treeNode)
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as NoteNode })
        }
        return expandedNodes
    }

}
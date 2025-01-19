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
    protected val nodesGroupedById = HashMap<String, NoteNode>()

    abstract fun createNewInRoot(createNodeDto: CreateNodeDto): NoteNode

    open fun createInto(createNodeDto: CreateNodeDto, targetNode: NoteNode): NoteNode {
        val node: NoteNode = service<CreateDtoToTreeNodeMapper>().toNoteNode(createNodeDto)

        val expandedNodes = getExpandedNodes(targetNode)

        targetNode.insert(
            node,
            targetNode.childCount
        )

        getTreeModel().reload(targetNode)

        expandNodes(expandedNodes)

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

    fun getSelectedNode(): NoteNode? = selectionPath?.lastPathComponent as? NoteNode

    fun openInEditor(node: NoteNode) {
        println("openInEditor: $node")
    }

    protected fun getRoot() = getTreeModel().root as NoteNode

    protected fun getTreeModel() = model as T

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

    private fun expandNodes(expandedNodes: ArrayList<NoteNode>) {
        expandedNodes.forEach { expandPath(TreeUtil.getPath(getRoot(), it)) }
    }

    abstract fun delete(id: String)

}
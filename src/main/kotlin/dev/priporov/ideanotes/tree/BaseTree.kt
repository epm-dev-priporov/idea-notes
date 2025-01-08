package dev.priporov.ideanotes.tree

import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.util.*
import javax.swing.tree.TreeModel

abstract class BaseTree<T : TreeModel> : Tree() {

    fun expandAll() {
        val list = LinkedList<FileTreeNode>()
        val root = model.root as FileTreeNode

        list.add(root)

        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isCollapsed(TreeUtil.getPath(root, treeNode))) {
                expandPath(TreeUtil.getPath(root, treeNode))
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as FileTreeNode })
        }
    }

    fun collapseAll() {
        val root = getRoot()

        getExpandedNodes(root).asSequence().filter { it != root }.forEach {
            collapsePath(TreeUtil.getPath(it.parent, it))
        }
    }

    fun getTreeModel() = model as T

    fun getSelectedNode(): FileTreeNode? = selectionPath?.lastPathComponent as? FileTreeNode

    fun openInEditor(node: FileTreeNode) {
        println("openInEditor: $node")
    }

    private fun getRoot() = getTreeModel().root as FileTreeNode

    private fun getExpandedNodes(node: FileTreeNode): ArrayList<FileTreeNode> {
        val list = LinkedList<FileTreeNode>()
        val expandedNodes = ArrayList<FileTreeNode>()
        val root = getRoot()

        list.add(node)
        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isExpanded(TreeUtil.getPath(root, treeNode))) {
                expandedNodes.add(treeNode)
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as FileTreeNode })
        }
        return expandedNodes
    }

}
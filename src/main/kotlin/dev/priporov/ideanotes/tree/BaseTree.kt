package dev.priporov.ideanotes.tree

import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
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

    fun getTreeModel() = model as T

}
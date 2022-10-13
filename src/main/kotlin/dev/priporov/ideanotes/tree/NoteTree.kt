package dev.priporov.ideanotes.tree

import com.intellij.ui.treeStructure.Tree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import javax.swing.tree.DefaultTreeModel


class NoteTree : Tree() {
    private val defaultModel = model as DefaultTreeModel
    private val root = FileTreeNode("test")

    init {
        defaultModel.setRoot(root)
    }

}
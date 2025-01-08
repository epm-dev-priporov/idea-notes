package dev.priporov.ideanotes.tree.model

import dev.priporov.ideanotes.tree.node.FileTreeNode
import javax.swing.tree.DefaultTreeModel

open class BaseNoteTreeModel(private val rootNode: FileTreeNode) : DefaultTreeModel(rootNode) {

    override fun getRoot(): FileTreeNode {
        return rootNode
    }

}
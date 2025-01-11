package dev.priporov.ideanotes.tree.node


import dev.priporov.ideanotes.tree.node.init.NodeType
import javax.swing.tree.DefaultMutableTreeNode

open class NoteNode(name: String? = null) : DefaultMutableTreeNode(name) {
    var type: NodeType? = null
    var id: String? = null
}
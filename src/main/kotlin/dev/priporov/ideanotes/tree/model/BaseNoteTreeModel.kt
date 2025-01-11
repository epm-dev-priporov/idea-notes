package dev.priporov.ideanotes.tree.model

import dev.priporov.ideanotes.tree.node.NoteNode
import javax.swing.tree.DefaultTreeModel

open class BaseNoteTreeModel(private val rootNode: NoteNode) : DefaultTreeModel(rootNode) {

    override fun getRoot(): NoteNode {
        return rootNode
    }

}
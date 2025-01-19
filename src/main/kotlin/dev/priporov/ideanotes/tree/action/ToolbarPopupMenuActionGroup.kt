package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.DefaultActionGroup
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.node.NoteNode

class ToolbarPopupMenuActionGroup(tree: BaseTree<*>) : DefaultActionGroup() {
    init {
        val targetNode = tree.selectionPath?.lastPathComponent as? NoteNode
        if (targetNode == null) {
            add(NewNodeActionGroup(tree, "New node"))
        } else {
            add(NewNodeActionGroup(tree, "New node"))
            add(AddNodeActionGroup(tree, targetNode, "Add child node"))
        }
    }

}

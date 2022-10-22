package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.noteplugin.component.dialog.EditDialog


class RenameNodeAction(
    private val tree: NoteTree,
    value: String
) : AnAction(value) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.getSelectedNode() ?: return
        EditDialog("Rename note", node.name) { value -> tree.renameNode(value, node) }.show()
    }

}

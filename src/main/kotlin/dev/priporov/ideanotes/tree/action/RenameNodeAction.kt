package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.dialog.TextFieldDialog


class RenameNodeAction(
    private val tree: BaseTree<*>,
    value: String? = null
) : AnAction(value) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.getSelectedNode() ?: return
        TextFieldDialog("Rename note", node.name) { value ->
            tree.renameNode(value, node)
        }.show()

    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

}

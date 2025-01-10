package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.tree.BaseTree


class RenameNodeAction(
    private val tree: BaseTree<*>,
    value: String? = null
) : AnAction(value) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.getSelectedNode() ?: return

    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

}

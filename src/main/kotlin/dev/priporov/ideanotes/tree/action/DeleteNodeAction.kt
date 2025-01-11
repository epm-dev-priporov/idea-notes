package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.node.NoteNode

class DeleteNodeAction(
    private val tree: BaseTree<*>,
    private val node: NoteNode,
    value: String
) : AnAction(value, "", Icons.PopUpMenu.DELETE_ICON) {

    override fun actionPerformed(e: AnActionEvent) {

    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

}
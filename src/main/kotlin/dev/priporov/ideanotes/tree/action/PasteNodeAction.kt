package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree

class PasteNodeAction(
    private val tree: BaseTree<*>,
    value: String? = null
) : AnAction(value, "", Icons.PopUpMenu.PASTE_ICON) {

    override fun actionPerformed(e: AnActionEvent) {

    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

}
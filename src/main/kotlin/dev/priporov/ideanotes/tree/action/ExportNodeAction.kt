package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.service.ExportService

class ExportNodeAction(
    private val tree: BaseTree<*>,
    value: String? = null
) : AnAction(value, "", Icons.ToolbarFactory.EXPORT_ICON) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.getSelectedNode() ?: return
        service<ExportService>().exportNotesToFile(tree, node)
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

}

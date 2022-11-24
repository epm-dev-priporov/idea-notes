package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.toolbar.NoteToolbarFactory
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.exporting.ExportService


class ExportNodeAction(
    private val tree: NoteTree,
    value: String? = null
) : AnAction(value, "", NoteToolbarFactory.exportIcon) {

    private val exportService = service<ExportService>()

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.getSelectedNode() ?: return
        exportService.exportNotesToFile(tree, node).invoke()
    }

}

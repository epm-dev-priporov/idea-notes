package dev.priporov.ideanotes

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import dev.priporov.ideanotes.main.TabbedPanel
import dev.priporov.ideanotes.tree.factory.NoteTreeFactory
import dev.priporov.ideanotes.tree.factory.ToolbarFactory
import dev.priporov.ideanotes.tree.panel.TreePanel


class NotesMainWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()

        val tree = service<NoteTreeFactory>().getInstance(project)
        val toolbar = service<ToolbarFactory>().getInstance(tree)

        val content = contentFactory.createContent(
            TabbedPanel(TreePanel(tree, toolbar)),
            "",
            false
        )

        toolWindow.contentManager.addContent(content)
    }

}
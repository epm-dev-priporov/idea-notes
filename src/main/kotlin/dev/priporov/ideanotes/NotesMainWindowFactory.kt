package dev.priporov.ideanotes

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import dev.priporov.ideanotes.main.TabbedPanel
import dev.priporov.ideanotes.tree.factory.AppNoteTreeFactory
import dev.priporov.ideanotes.tree.factory.ProjectNoteTreeFactory
import dev.priporov.ideanotes.tree.factory.ToolbarFactory
import dev.priporov.ideanotes.tree.panel.TreePanel


class NotesMainWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val appTree = service<AppNoteTreeFactory>().getInstance(project)
        val appToolbar = service<ToolbarFactory>().getInstance(appTree)

        val projectNoteTree = service<ProjectNoteTreeFactory>().getInstance(project)
        val projectToolbar = service<ToolbarFactory>().getInstance(projectNoteTree)

        val content = ContentFactory.getInstance().createContent(
            TabbedPanel(
                TreePanel(appTree, appToolbar), TreePanel(projectNoteTree, projectToolbar)
            ),
            "",
            false
        )

        toolWindow.contentManager.addContent(content)
    }

}
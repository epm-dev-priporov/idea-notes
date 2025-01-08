package dev.priporov.ideanotes

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import dev.priporov.ideanotes.main.TabbedPanel
import dev.priporov.ideanotes.tree.ProjectNoteTree
import dev.priporov.ideanotes.tree.factory.NoteTreeFactory
import dev.priporov.ideanotes.tree.factory.ToolbarFactory
import dev.priporov.ideanotes.tree.panel.TreePanel


class NotesMainWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()

        val appTree = service<NoteTreeFactory>().getAppInstance(project)
        val appToolbar = service<ToolbarFactory>().getInstance(appTree)

        val projectNoteTree = project.getService(ProjectNoteTree::class.java)
        val projectToolbar = service<ToolbarFactory>().getInstance(projectNoteTree)

        val content = contentFactory.createContent(
            TabbedPanel(
                TreePanel(appTree, appToolbar), TreePanel(projectNoteTree, projectToolbar)),
            "",
            false
        )

        toolWindow.contentManager.addContent(content)
    }

}
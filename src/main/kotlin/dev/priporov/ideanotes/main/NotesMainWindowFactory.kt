package dev.priporov.ideanotes.main

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class NotesMainWindowFactory : ToolWindowFactory {
    private val main = MainNoteToolWindow()//service<MainNoteToolWindow>()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(main, "", false)
        toolWindow.contentManager.addContent(content)
    }

}
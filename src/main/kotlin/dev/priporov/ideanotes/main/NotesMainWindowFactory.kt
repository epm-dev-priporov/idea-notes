package dev.priporov.ideanotes.main

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import dev.priporov.ideanotes.listener.NoteKeyListener
import dev.priporov.ideanotes.listener.TreeMouseListener
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.NoteCellRenderer
import dev.priporov.ideanotes.util.TreeModelProvider
import dev.priporov.ideanotes.handler.DragAndDropTransferHandler


class NotesMainWindowFactory : ToolWindowFactory {
    private val treeModelProvider = service<TreeModelProvider>()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val tree = project.getService(NoteTree::class.java).apply {
            transferHandler = DragAndDropTransferHandler()
            setCellRenderer(NoteCellRenderer())
            addMouseListener(TreeMouseListener(this))
            addKeyListener(NoteKeyListener(this))
            treeModelProvider.setCommonModel(this)
        }

        val content = contentFactory.createContent(MainNoteToolWindow(tree), "", false)
        toolWindow.contentManager.addContent(content)
    }

}
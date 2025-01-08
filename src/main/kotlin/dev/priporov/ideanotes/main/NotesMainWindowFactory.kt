package dev.priporov.ideanotes.main

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import dev.priporov.ideanotes.handler.DragAndDropTransferHandler
import dev.priporov.ideanotes.listener.NoteKeyListener
import dev.priporov.ideanotes.listener.TreeMouseListener
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.NoteCellRenderer
import dev.priporov.ideanotes.tree.importing.ImportService
import dev.priporov.ideanotes.tree.state.StateService
import dev.priporov.ideanotes.tree.state.TreeInitializer
import dev.priporov.ideanotes.util.TreeModelProvider


class NotesMainWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val tree: NoteTree = project.getService(NoteTree::class.java).apply {
            transferHandler = DragAndDropTransferHandler()
            setCellRenderer(NoteCellRenderer())
            addMouseListener(TreeMouseListener(this))
            addKeyListener(NoteKeyListener(this))
            service<TreeModelProvider>().setCommonModel(this)
        }

        importTreeNodes(tree)
        tree.insertFilesFromQueue()

        val content = contentFactory.createContent(MainNoteToolWindow(tree), "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun importTreeNodes(tree: NoteTree) {
        service<TreeInitializer>().initTreeModelFromState(
            service<StateService>().getTreeState(),
            tree
        )
    }

}
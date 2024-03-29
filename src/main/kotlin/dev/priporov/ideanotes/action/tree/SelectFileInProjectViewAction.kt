package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.VirtualFileContainer

class SelectFileInProjectViewAction(
    private val textEditor: TextEditor,
    private val virtualFileContainerService: VirtualFileContainer
) : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val node = virtualFileContainerService.getNode(textEditor.file)
        val tree: NoteTree? = e.project?.getService(NoteTree::class.java)
        if (node != null && tree != null) {
            TreeUtil.selectInTree(node, true, tree)
        }
    }
}
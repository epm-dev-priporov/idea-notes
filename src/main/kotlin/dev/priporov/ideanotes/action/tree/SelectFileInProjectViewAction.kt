package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.VirtualFileContainer

class SelectFileInProjectViewAction(
    private val fileEditor: FileEditor,
    private val virtualFileContainerService: VirtualFileContainer
) : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val node = virtualFileContainerService.getNode(fileEditor.file)
        val tree: NoteTree? = e.project?.getService(NoteTree::class.java)
        if (node != null && tree != null) {
            TreeUtil.selectInTree(node, true, tree)
            TreeUtil.selectNode(tree, node)
        }
    }
}
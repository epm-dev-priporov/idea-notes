package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.noteplugin.component.dialog.OkDialog

class DeleteNodeAction(
    private val tree: NoteTree,
    private val node: FileTreeNode,
    value: String
) : AnAction(value) {

    override fun actionPerformed(e: AnActionEvent) {
        OkDialog("Delete node") { tree.delete(node) }.show()
    }

}

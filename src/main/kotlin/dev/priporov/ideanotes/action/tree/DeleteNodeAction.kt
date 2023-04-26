package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.IconUtils
import dev.priporov.ideanotes.dialog.OkDialog

private val DELETE_ICON =  IconUtils.toIcon("menu/deleteIcon.png")

class DeleteNodeAction(
    private val tree: NoteTree,
    private val node: FileTreeNode,
    value: String
) : AnAction(value, "", DELETE_ICON) {

    override fun actionPerformed(e: AnActionEvent) {
        OkDialog("Delete node") { tree.delete(node) }.show()
    }

}

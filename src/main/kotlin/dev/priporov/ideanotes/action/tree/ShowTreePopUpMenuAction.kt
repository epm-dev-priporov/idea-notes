package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.MousePopupMenuActionGroup
import dev.priporov.ideanotes.tree.common.TreePopUpMenuManager
import dev.priporov.ideanotes.tree.node.FileTreeNode


class ShowTreePopUpMenuAction(private val tree: NoteTree) : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (tree.selectionPath?.lastPathComponent !is FileTreeNode) return

        val offsetY = tree.ui.getPathBounds(tree, tree.selectionPath).y
        val locationOnScreen = tree.locationOnScreen
        locationOnScreen.setLocation(locationOnScreen.x, locationOnScreen.y + offsetY)

        TreePopUpMenuManager.createPopUpMenu(
            tree,
            locationOnScreen,
            MousePopupMenuActionGroup(tree, true)
        )
    }

}
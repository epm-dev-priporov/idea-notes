package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.menu.MousePopUpMenu
import dev.priporov.ideanotes.tree.menu.TreePopUpMenuManager
import dev.priporov.ideanotes.tree.node.NoteNode

class ShowTreePopUpMenuAction(private val tree: BaseTree<*>) : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (tree.selectionPath?.lastPathComponent !is NoteNode) return

        val offsetY = tree.ui.getPathBounds(tree, tree.selectionPath).y
        val locationOnScreen = tree.locationOnScreen
        locationOnScreen.setLocation(locationOnScreen.x, locationOnScreen.y + offsetY)

        TreePopUpMenuManager.createPopUpMenu(
            tree,
            locationOnScreen,
            MousePopUpMenu(tree, true)
        )
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

}
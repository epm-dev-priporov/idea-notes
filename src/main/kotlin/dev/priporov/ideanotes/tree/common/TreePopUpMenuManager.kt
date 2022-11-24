package dev.priporov.ideanotes.tree.common

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopupStep
import com.intellij.ui.awt.RelativePoint
import dev.priporov.ideanotes.action.tree.*
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.awt.Point

class TreePopUpMenuManager {
    companion object {
        fun createPopUpMenu(tree: NoteTree, point: Point, actionGroup: DefaultActionGroup) {

            val factory = JBPopupFactory.getInstance()

            val popupStep: ListPopupStep<Any> = factory.createActionsStep(
                actionGroup,
                DataManager.getInstance().getDataContext(tree),
                ActionPlaces.POPUP,
                false,
                true,
                "",
                tree,
                true,
                0,
                true
            )

            factory.createListPopup(popupStep).show(RelativePoint(point))
        }
    }
}


class ToolbarPopupMenuActionGroup(tree: NoteTree) : DefaultActionGroup() {
    init {
        val targetNode = tree.selectionPath?.lastPathComponent as? FileTreeNode
        if (targetNode == null) {
            add(NewNodeActionGroup(tree, "New node"))
        } else {
            add(NewNodeActionGroup(tree, "New node"))
            add(AddNodeActionGroup(tree, targetNode, "Add child node"))
        }
    }
}

class MousePopupMenuActionGroup(tree: NoteTree, selectedNodeClicked: Boolean) : DefaultActionGroup() {
    init {
        val targetNode = tree.selectionPath?.lastPathComponent as? FileTreeNode

        if (targetNode == null || !selectedNodeClicked) {
            add(NewNodeActionGroup(tree, "New node"))
            add(PasteNodeAction(tree, "Past"))
        } else {
            add(NewNodeActionGroup(tree, "New node"))
            add(AddNodeActionGroup(tree, targetNode, "Add child node"))
            add(CutNodeAction(tree, "Cut"))
            add(CopyNodeAction(tree, "Copy"))
            add(PasteNodeAction(tree, "Past"))
            add(RenameNodeAction(tree, "Rename"))
            add(ExportNodeAction(tree, "Export"))
            add(DeleteNodeAction(tree, targetNode, "Delete"))
        }
    }

}
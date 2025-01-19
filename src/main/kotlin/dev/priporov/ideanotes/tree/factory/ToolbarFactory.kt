package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.ui.AnActionButton
import com.intellij.ui.AnActionButtonRunnable
import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.action.AddNodeActionGroup
import dev.priporov.ideanotes.tree.action.NewNodeActionGroup
import dev.priporov.ideanotes.tree.menu.TreePopUpMenuManager
import dev.priporov.ideanotes.tree.node.NoteNode
import javax.swing.JPanel

class ToolbarFactory {

    fun getInstance(tree: BaseTree<*>): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(tree)

        decorator.setAddAction {
            TreePopUpMenuManager.createPopUpMenu(tree, tree.locationOnScreen, ToolbarPopupMenuActionGroup(tree))
        }

        decorator.addExtraAction(
            object : AnAction("Expand All", "", Icons.ToolbarFactory.EXPAND_ICON) {
                override fun actionPerformed(e: AnActionEvent) {
                    tree.expandAll()
                }
            },
        )

        decorator.addExtraAction(
            object : AnAction("Collapse All", "", Icons.ToolbarFactory.COLLAPSE_ICON) {
                override fun actionPerformed(e: AnActionEvent) {
                    tree.collapseAll()
                }
            },
        )
        return decorator.createPanel()
    }

}

class ToolbarPopupMenuActionGroup(tree: BaseTree<*>) : DefaultActionGroup() {
    init {
        val targetNode = tree.selectionPath?.lastPathComponent as? NoteNode
        if (targetNode == null) {
            add(NewNodeActionGroup(tree, "New node"))
        } else {
            add(NewNodeActionGroup(tree, "New node"))
            add(AddNodeActionGroup(tree, targetNode, "Add child node"))
        }
    }
}

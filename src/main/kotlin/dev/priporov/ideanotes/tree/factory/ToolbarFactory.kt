package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.action.ToolbarPopupMenuActionGroup
import dev.priporov.ideanotes.tree.dialog.OkDialog
import dev.priporov.ideanotes.tree.menu.TreePopUpMenuManager
import dev.priporov.ideanotes.tree.node.NoteNode
import javax.swing.JPanel

class ToolbarFactory {

    fun getInstance(tree: BaseTree<*>): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(tree)

        decorator.setAddAction {
            TreePopUpMenuManager.createPopUpMenu(tree, tree.locationOnScreen, ToolbarPopupMenuActionGroup(tree))
        }

        decorator.setRemoveAction {
            OkDialog("Delete node") {
                tree.getSelectedNodes(NoteNode::class.java, null).filterNotNull().forEach { node ->
                    tree.delete(node.id!!)
                }
            }.show()
        }

        decorator.addExtraActions(
            object : AnAction("Expand All", "", Icons.ToolbarFactory.EXPAND_ICON) {
                override fun actionPerformed(e: AnActionEvent) {
                    tree.expandAll()
                }
            },
            object : AnAction("Collapse All", "", Icons.ToolbarFactory.COLLAPSE_ICON) {
                override fun actionPerformed(e: AnActionEvent) {
                    tree.collapseAll()
                }
            },
            object : AnAction("Import", "", Icons.ToolbarFactory.IMPORT_ICON) {
                override fun actionPerformed(e: AnActionEvent) {
                    TODO("Not yet implemented")
                }
            },
            object : AnAction("Export All", "", Icons.ToolbarFactory.EXPORT_ICON) {
                override fun actionPerformed(e: AnActionEvent) {
                    TODO("Not yet implemented")
                }
            },
            object : AnAction("Settings", "", Icons.ToolbarFactory.SETTINGS_ICON) {
                override fun actionPerformed(e: AnActionEvent) {
                    TODO("Not yet implemented")
                }
            }
        )

        return decorator.createPanel()
    }

}

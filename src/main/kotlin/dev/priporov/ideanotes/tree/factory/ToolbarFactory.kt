package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import javax.swing.JPanel

@Service
class ToolbarFactory {

    fun getInstance(tree: BaseTree<*>): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(tree)
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
                    tree.expandAll()
                }
            },
        )
        return decorator.createPanel()
    }

}
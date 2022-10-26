package dev.priporov.ideanotes.toolbar

import com.intellij.ui.AnActionButton
import com.intellij.ui.AnActionButtonRunnable
import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ToolbarPopupMenuActionGroup
import dev.priporov.ideanotes.tree.common.TreePopUpMenuManager
import dev.priporov.noteplugin.component.dialog.OkDialog
import javax.swing.JPanel

object NoteToolbarFactory {

    fun getInstance(tree: NoteTree): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(tree)
        decorator.setAddAction(NoteToolbarAction {
            TreePopUpMenuManager.createPopUpMenu(tree, tree.locationOnScreen, ToolbarPopupMenuActionGroup(tree))
        })

        decorator.setRemoveAction(NoteToolbarAction {
            OkDialog("Delete node") { tree.delete(tree.getSelectedNode()!!) }.show()
        })

        return decorator.createPanel()
    }

}

class NoteToolbarAction(private val function: () -> Unit) : AnActionButtonRunnable {
    override fun run(t: AnActionButton) = function.invoke()
}
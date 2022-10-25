package dev.priporov.ideanotes.toolbar

import com.intellij.ui.AnActionButton
import com.intellij.ui.AnActionButtonRunnable
import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ToolbarPopupMenuActionGroup
import dev.priporov.ideanotes.tree.common.TreePopUpMenuManager
import javax.swing.JPanel

object NoteToolbarFactory {

    fun getInstance(tree: NoteTree): JPanel {
        val decorator = ToolbarDecorator.createDecorator(tree)
        decorator.setAddAction(NewNoteAnActionButtonRunnable(tree))

        return decorator.createPanel()
    }

}

class NewNoteAnActionButtonRunnable(private val tree: NoteTree) : AnActionButtonRunnable {
    override fun run(t: AnActionButton) {
        TreePopUpMenuManager.createPopUpMenu(tree, tree.locationOnScreen, ToolbarPopupMenuActionGroup(tree))
    }
}
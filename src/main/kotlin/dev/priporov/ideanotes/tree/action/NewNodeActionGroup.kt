package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree


class NewNodeActionGroup(tree: BaseTree<*>, nodeName: String) : DefaultActionGroup() {
    init {
        templatePresentation.text = nodeName
        isPopup = true
        add(NewNodeAction(tree))
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        event.presentation.setIcon(Icons.PopUpMenu.ADD_NODE_ICON)
        super.update(event)
    }

}


class NewNodeAction(
    private val tree: BaseTree<*>,

    ) : AnAction("Add", "", null) {

    override fun actionPerformed(e: AnActionEvent) {

    }

}

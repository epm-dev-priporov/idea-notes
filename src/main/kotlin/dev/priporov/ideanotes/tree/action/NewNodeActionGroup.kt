package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.node.init.NodeDefinition
import dev.priporov.ideanotes.tree.node.init.NodeDefinitionService
import dev.priporov.ideanotes.tree.node.init.NodeType


class NewNodeActionGroup(tree: BaseTree<*>, nodeName: String) : DefaultActionGroup() {
    init {
        templatePresentation.text = nodeName
        isPopup = true
        service<NodeDefinitionService>().nodeDefinitions.values.forEach { definition ->
            add(NewNodeAction(tree, definition))
        }

    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        event.presentation.setIcon(Icons.PopUpMenu.ADD_NODE_ICON)
        super.update(event)
    }
}


class NewNodeAction(
    private val tree: BaseTree<*>,
    definition: NodeDefinition,
    ) : AnAction(definition.definition, "", definition.getRequiredIcon()) {

    override fun actionPerformed(e: AnActionEvent) {

    }

}

package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.dialog.TextFieldDialog
import dev.priporov.ideanotes.tree.factory.CreateNodeDtoFactory
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.NodeDefinitionDto
import dev.priporov.ideanotes.tree.service.NodeDefinitionService

class AddNodeActionGroup(tree: BaseTree<*>, targetNode: NoteNode, actionName: String) : DefaultActionGroup() {
    init {
        templatePresentation.text = actionName
        isPopup = true
        service<NodeDefinitionService>().getSupportedDefinitionsForCreation().forEach { definition ->
            add(AddChildNodeAction(tree, definition, targetNode))
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        event.presentation.setIcon(Icons.PopUpMenu.ADD_NODE_ICON)
        super.update(event)
    }
}

class AddChildNodeAction(
    private val tree: BaseTree<*>,
    private val definition: NodeDefinitionDto,
    private val targetNode: NoteNode,
) : AnAction(definition.definition, "", definition.getRequiredIcon()) {

    override fun actionPerformed(e: AnActionEvent) {
        TextFieldDialog("New note") { name ->
            tree.createInto(
                service<CreateNodeDtoFactory>().toCreateNodeDto(name, definition),
                targetNode
            )
        }.show()
    }

}


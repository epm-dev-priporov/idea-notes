package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.DOCKERFILE
import dev.priporov.ideanotes.tree.common.ExtensionData
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.importing.SoftLinkService
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.IconUtils
import dev.priporov.ideanotes.dialog.EditDialog

private val ADD_NODE_ICON = IconUtils.toIcon("menu/addNodeIcon.png")

class AddNodeActionGroup(tree: NoteTree, targetNode: FileTreeNode, actionName: String) : DefaultActionGroup() {
    init {
        isPopup = true
        templatePresentation.text = actionName
        ExtensionFileHelper.SORTED_EXTENSIONS.forEach {
            add(AddChildNodeAction(tree, targetNode, it))
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.setIcon(ADD_NODE_ICON)
        super.update(event)
    }

}


class AddChildNodeAction(
    private val tree: NoteTree,
    private val targetNode: FileTreeNode,
    private val extensionData: ExtensionData
) : AnAction(extensionData.definition, "", extensionData.getRequiredLeafIcon()) {

    private val softLinkService = service<SoftLinkService>()

    override fun actionPerformed(e: AnActionEvent) {
        val extension = extensionData.extension

        when (extensionData.type) {
            NodeType.SOFT_LINK -> softLinkService.makeSoftLink(targetNode, tree)
            NodeType.DOCKERFILE -> tree.insert(NodeCreationInfo(targetNode, DOCKERFILE, extension, NodeType.DOCKERFILE))
            NodeType.DOCKER_COMPOSE -> tree.insert(NodeCreationInfo(targetNode, "docker_compose", extension, NodeType.DOCKER_COMPOSE))
            else -> {
                EditDialog("Add node") { value ->
                    tree.insert(NodeCreationInfo(targetNode, value, extension, extensionData.type))
                }.show()
            }
        }
    }

}


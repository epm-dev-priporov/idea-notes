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
import dev.priporov.ideanotes.util.IconUtils
import dev.priporov.noteplugin.component.dialog.EditDialog

private val ADD_ICON = IconUtils.toIcon("menu/addIcon.png")

class NewNodeActionGroup(tree: NoteTree, nodeName: String) : DefaultActionGroup() {
    init {
        templatePresentation.text = nodeName
        isPopup = true
        ExtensionFileHelper.SORTED_EXTENSIONS.forEach {
            add(NewNodeAction(tree, it))
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.setIcon(ADD_ICON)
        super.update(event)
    }

}

class NewNodeAction(
    private val tree: NoteTree,
    private val extensionData: ExtensionData
) : AnAction(extensionData.definition, "", extensionData.getRequiredLeafIcon()) {

    private val softLinkService = service<SoftLinkService>()

    override fun actionPerformed(e: AnActionEvent) {
        when (extensionData.type) {
            NodeType.SOFT_LINK -> softLinkService.makeSoftLink()
            NodeType.DOCKERFILE -> insertToRoot(DOCKERFILE, extensionData)
            NodeType.DOCKER_COMPOSE -> insertToRoot("docker_compose", extensionData)
            else -> {
                EditDialog("New node") { value ->
                    insertToRoot(value, extensionData)
                }.show()
            }
        }
    }

    private fun insertToRoot(value: String, extensionData: ExtensionData) {
        tree.insert(NodeCreationInfo(tree.root, value, extensionData.extension, extensionData.type))
    }

}

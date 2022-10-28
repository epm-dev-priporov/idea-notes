package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.IconUtils
import dev.priporov.noteplugin.component.dialog.EditDialog
import javax.swing.Icon


class AddNodeActionGroup(tree: NoteTree, targetNode: FileTreeNode, actionName: String) : DefaultActionGroup() {
    init {
        isPopup = true
        templatePresentation.text = actionName
        ExtensionFileHelper.EXTENSIONS.values.forEach {
            add(AddChildNodeAction(tree, targetNode, it.definition, it.extension, it.leafIcon))
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.setIcon(IconUtils.toIcon("menu/addNodeIcon.png"))
        super.update(event)
    }

}


class AddChildNodeAction(
    private val tree: NoteTree,
    private val targetNode: FileTreeNode,
    definition: String,
    private val extension: String,
    icon: Icon
) : AnAction(definition, "", icon) {

    override fun actionPerformed(e: AnActionEvent) {
        EditDialog("Add node") { value ->
            tree.insert(NodeCreationInfo(targetNode, value, extension))
        }.show()
    }

}


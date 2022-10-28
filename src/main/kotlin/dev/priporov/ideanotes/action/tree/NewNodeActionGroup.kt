package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper
import dev.priporov.ideanotes.util.IconUtils
import dev.priporov.noteplugin.component.dialog.EditDialog
import javax.swing.Icon

class NewNodeActionGroup(tree: NoteTree, nodeName: String) : DefaultActionGroup() {
    init {
        templatePresentation.text = nodeName
        isPopup = true
        ExtensionFileHelper.EXTENSIONS.values.forEach {
            add(NewNodeAction(tree, it.extension, it.definition, it.leafIcon))
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.setIcon(IconUtils.toIcon("menu/addIcon.png"))
        super.update(event)
    }

}

class NewNodeAction(
    private val tree: NoteTree,
    private val extension: String = "txt",
    definition: String,
    icon: Icon
) : AnAction(definition, "", icon) {

    override fun actionPerformed(e: AnActionEvent) {
        EditDialog("New node") {
                value -> insertToRoot(value)
        }.show()
    }

    private fun insertToRoot(value: String) {
        val root = tree.root ?: return
        tree.insert(NodeCreationInfo(root, value, extension))
    }

}

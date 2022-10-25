package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper
import dev.priporov.noteplugin.component.dialog.EditDialog

class NewNodeActionGroup(tree: NoteTree, nodeName: String) : DefaultActionGroup() {
    init {
        templatePresentation.text = nodeName
        isPopup = true
        ExtensionFileHelper.EXTENSIONS.forEach {
            add(NewNodeAction(tree, it.extension, it.definition))
        }
    }

}

class NewNodeAction(
    private val tree: NoteTree,
    private val extension: String = "txt",
    definition: String
) : AnAction(definition) {

    override fun actionPerformed(e: AnActionEvent) {
        EditDialog("New node") {
                value -> insertToRoot(value)
        }
            .requestFocusInWindow()
            .show()
    }

    private fun insertToRoot(value: String) {
        val root = tree.root ?: return
        tree.insert(NodeCreationInfo(root, value, extension))
    }

}

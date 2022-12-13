package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.DOCKERFILE
import dev.priporov.ideanotes.tree.common.DOCKER_COMPOSE
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper
import dev.priporov.ideanotes.util.IconUtils
import dev.priporov.noteplugin.component.dialog.EditDialog
import javax.swing.Icon

private val ADD_ICON = IconUtils.toIcon("menu/addIcon.png")

class NewNodeActionGroup(tree: NoteTree, nodeName: String) : DefaultActionGroup() {
    init {
        templatePresentation.text = nodeName
        isPopup = true
        ExtensionFileHelper.SORTED_EXTENSIONS.forEach {
            add(NewNodeAction(tree, it.extension, it.definition, it.leafIcon))
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.setIcon(ADD_ICON)
        super.update(event)
    }

}

class NewNodeAction(
    private val tree: NoteTree,
    private val extension: String = "txt",
    private val definition: String,
    icon: Icon
) : AnAction(definition, "", icon) {

    override fun actionPerformed(e: AnActionEvent) {
        when (definition) {
            DOCKERFILE -> insertToRoot(DOCKERFILE)
            DOCKER_COMPOSE -> insertToRoot("docker_compose")
            else -> {
                EditDialog("New node") { value ->
                    insertToRoot(value)
                }.show()
            }
        }
    }

    private fun insertToRoot(value: String) {
        tree.insert(NodeCreationInfo(tree.root, value, extension))
    }

}

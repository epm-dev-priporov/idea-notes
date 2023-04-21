package dev.priporov.ideanotes.action.tree

import com.intellij.ide.impl.DataManagerImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.project.ProjectManager
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.noteplugin.component.dialog.EditDialog


class OpenInEditorAction(
    private val tree: NoteTree,
    value: String? = null
) : AnAction(value) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.getSelectedNode() ?: return
        tree.openInEditor(node)
    }

}

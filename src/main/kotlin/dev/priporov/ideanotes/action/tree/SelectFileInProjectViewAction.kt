package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.impl.text.TextEditorCustomizer
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.VirtualFileContainer
import dev.priporov.ideanotes.util.TreeModelProvider

class SelectFileInProjectViewAction(
    private val textEditor: TextEditor,
    private val virtualFileContainerService: VirtualFileContainer,
    private val tree: NoteTree?
) : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val node = virtualFileContainerService.getNode(textEditor.file)
        if (node != null && tree != null) {
            TreeUtil.selectInTree(node, true, tree)
        }
    }
}

class UnnamedConfigurableCustom : TextEditorCustomizer {

    override fun customize(textEditor: TextEditor) {
        val virtualFileContainerService = service<VirtualFileContainer>()
        val virtualFile = textEditor.file
        virtualFileContainerService.initLateIfNeeded(virtualFile){
            applyAction(textEditor, virtualFileContainerService)
        }
        if (virtualFileContainerService.isNote(virtualFile)) {
            applyAction(textEditor, virtualFileContainerService)
        }
    }

    private fun applyAction(
        textEditor: TextEditor,
        virtualFileContainerService: VirtualFileContainer
    ) {
        val service = service<TreeModelProvider>()

        ActionUtil.getShortcutSet("SelectInProjectView").shortcuts.also { shortcutSet ->
            SelectFileInProjectViewAction(
                textEditor,
                virtualFileContainerService,
                service.tree
            ).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), textEditor.component)
        }
    }

}
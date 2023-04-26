package dev.priporov.ideanotes.extension

import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.impl.text.TextEditorCustomizer
import dev.priporov.ideanotes.action.tree.SelectFileInProjectViewAction
import dev.priporov.ideanotes.tree.common.VirtualFileContainer
import dev.priporov.ideanotes.util.TreeModelProvider

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
        ActionUtil.getShortcutSet("SelectInProjectView").shortcuts.also { shortcutSet ->
            SelectFileInProjectViewAction(
                textEditor,
                virtualFileContainerService
            ).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), textEditor.component)
        }
    }

}
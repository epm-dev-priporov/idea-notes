package dev.priporov.ideanotes.extension

import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.action.tree.SelectFileInProjectViewAction
import dev.priporov.ideanotes.tree.common.VirtualFileContainer

class OpenedFileListener : FileEditorManagerListener {

    companion object {
        fun applyAction(
            textEditor: FileEditor,
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

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        val virtualFileContainerService = service<VirtualFileContainer>()
        val editors: Array<FileEditor> = source.getEditors(file)
        editors.forEach { editor: FileEditor ->
            if (editor != null) {
                virtualFileContainerService.initLateIfNeeded(file) {
                    applyAction(editor, virtualFileContainerService)
                }
                if (virtualFileContainerService.isNote(file)) {
                    applyAction(editor, virtualFileContainerService)
                }
                virtualFileContainerService.getNode(file)?.apply {
                    this.editor = editor
                }
            }
        }
        super.fileOpened(source, file)
    }

}
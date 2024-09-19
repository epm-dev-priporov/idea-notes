package dev.priporov.ideanotes.extension

import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileOpenedSyncListener
import com.intellij.openapi.fileEditor.ex.FileEditorWithProvider
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.action.tree.SelectFileInProjectViewAction
import dev.priporov.ideanotes.tree.common.VirtualFileContainer

class OpenedFileListener : FileOpenedSyncListener {

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

    override fun fileOpenedSync(
        source: FileEditorManager,
        file: VirtualFile,
        editorsWithProviders: List<FileEditorWithProvider>
    ) {
        val virtualFileContainerService = service<VirtualFileContainer>()

        editorsWithProviders.forEach {
            val editor = it.fileEditor
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

}
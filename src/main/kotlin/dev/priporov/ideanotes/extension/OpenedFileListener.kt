package dev.priporov.ideanotes.extension

import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.util.Pair
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

//    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
//        val virtualFileContainerService = service<VirtualFileContainer>()
//        val editors: Array<FileEditor> = source.getAllEditors(file)
//        editors.forEach { editor: FileEditor ->
//            virtualFileContainerService.initLateIfNeeded(file) {
//                applyAction(editor, virtualFileContainerService)
//            }
//            if (virtualFileContainerService.isNote(file)) {
//                applyAction(editor, virtualFileContainerService)
//            }
//            virtualFileContainerService.getNode(file)?.apply {
//                this.editor = editor
//            }
//        }
//        super.fileOpened(source, file)
//    }

    override fun fileOpenedSync(
        source: FileEditorManager,
        file: VirtualFile,
        editors: Pair<Array<FileEditor>, Array<FileEditorProvider>>
    ) {
        val virtualFileContainerService = service<VirtualFileContainer>()
//        val editors: Array<FileEditor> = source.getAllEditors(file)

        editors.first?.forEach { editor: FileEditor ->
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
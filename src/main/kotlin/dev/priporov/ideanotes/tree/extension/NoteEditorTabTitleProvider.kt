package dev.priporov.ideanotes.tree.extension

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.impl.EditorTabTitleProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.container.VirtualFileContainer

class NoteEditorTabTitleProvider : EditorTabTitleProvider {

    private val virtualFileContainer = service<VirtualFileContainer>()

    override fun getEditorTabTitle(project: Project, file: VirtualFile): String? {
        if (virtualFileContainer.isNote(file)) {
            return file.presentableName.substringBeforeLast("_")
        }

        return null
    }
}
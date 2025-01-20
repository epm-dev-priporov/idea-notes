package dev.priporov.ideanotes.tree.extension

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.impl.NonProjectFileWritingAccessExtension
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.container.VirtualFileContainer

class NoteIgnoredFileWritingAccessExtension : NonProjectFileWritingAccessExtension {
    private val virtualFileContainer = service<VirtualFileContainer>()

    override fun isWritable(file: VirtualFile) = virtualFileContainer.isNote(file)
}
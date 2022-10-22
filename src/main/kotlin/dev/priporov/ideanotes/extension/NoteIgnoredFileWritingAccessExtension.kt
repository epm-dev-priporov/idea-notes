package dev.priporov.ideanotes.extension

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.impl.NonProjectFileWritingAccessExtension
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.common.VirtualFileContainer

class NoteIgnoredFileWritingAccessExtension : NonProjectFileWritingAccessExtension {
    private val virtualFileContainer = service<VirtualFileContainer>()

    override fun isWritable(file: VirtualFile) = virtualFileContainer.isNote(file)
}
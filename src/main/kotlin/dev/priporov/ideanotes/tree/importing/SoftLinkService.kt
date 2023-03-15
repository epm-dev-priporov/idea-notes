package dev.priporov.ideanotes.tree.importing

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile

class SoftLinkService {
    fun makeSoftLink() {
        val descriptor = fileChooserDescriptor()

        FileChooser.chooseFile(descriptor, null, null)?.also { virtualFile: VirtualFile ->
            val file = virtualFile.toNioPath().toFile()

        }
    }

    private fun fileChooserDescriptor() = FileChooserDescriptor(
        true,
        false,
        true,
        true,
        false,
        false
    )

}
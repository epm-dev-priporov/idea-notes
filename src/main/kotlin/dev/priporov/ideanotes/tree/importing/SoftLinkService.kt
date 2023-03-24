package dev.priporov.ideanotes.tree.importing

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.dto.NodeSoftLinkCreationInfo
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.nio.file.Files

class SoftLinkService {



    fun makeSoftLink(targetNode: FileTreeNode, tree: NoteTree) {
        val descriptor = fileChooserDescriptor()

        FileChooser.chooseFile(descriptor, null, null)?.also { virtualFile: VirtualFile ->
            val file = virtualFile.toNioPath().toFile()
            val nodeCreationInfo = NodeSoftLinkCreationInfo(targetNode, file)
            tree.insert(nodeCreationInfo)
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
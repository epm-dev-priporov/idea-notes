package dev.priporov.ideanotes.tree.node


import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.node.dto.NodeType
import javax.swing.tree.DefaultMutableTreeNode

open class NoteNode(var name: String? = null) : DefaultMutableTreeNode(name) {
    var type: NodeType? = null
    var id: String? = null
    var file: VirtualFile? = null
}
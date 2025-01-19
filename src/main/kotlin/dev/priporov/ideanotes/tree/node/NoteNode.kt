package dev.priporov.ideanotes.tree.node


import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.node.dto.NodeType
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.MutableTreeNode

open class NoteNode(
    var name: String? = null,
    open var id: String? = null
) : DefaultMutableTreeNode(name) {
    var type: NodeType = NodeType.UNKNOWN
    var file: VirtualFile? = null

    override fun insert(newChild: MutableTreeNode?, childIndex: Int) {
        super.insert(newChild, childIndex)
    }
}
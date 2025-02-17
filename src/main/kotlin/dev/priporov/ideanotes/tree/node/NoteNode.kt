package dev.priporov.ideanotes.tree.node


import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.node.dto.NodeType
import java.util.*
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

    fun getChildren(): MutableSet<NoteNode> {
        val nodes = HashSet<NoteNode>()
        val queue = LinkedList<NoteNode>()
        queue.add(this)

        while (queue.isNotEmpty()) {
            val node = queue.pop()
            node.children().asSequence().forEach { queue.add(it as NoteNode) }
            nodes.add(node)
        }
        return nodes
    }

}
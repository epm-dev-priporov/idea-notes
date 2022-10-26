package dev.priporov.ideanotes.dto

import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.state.NodeInfo
import dev.priporov.ideanotes.util.FileNodeUtils

class NodeCutData(
    node: FileTreeNode,
    tree: NoteTree
) {
    val nodeInfo: NodeInfo = NodeInfo(node)
    var children: List<NodeCutData>? = null
    val content: ByteArray

    init {
        children = node.children().asSequence().mapNotNull { NodeCutData(it as FileTreeNode, tree) }.toList()
        content = FileNodeUtils.readFileContentByteArray(tree, node.getFile())
    }

}
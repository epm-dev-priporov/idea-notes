package dev.priporov.ideanotes.tree.node.dto

import dev.priporov.ideanotes.tree.BaseTree

class NodeCutData {
    var id: String? = null
    var name: String? = null
    var type: NodeType = NodeType.UNKNOWN
    var content:ByteArray? = null
    var children: List<NodeCutData>? = null
    var tree: BaseTree<*>? = null

    init {
//        children = node.children().asSequence().mapNotNull { NodeCutData(it as FileTreeNode, tree) }.toList()
//        content = FileNodeUtils.readFileContentByteArray(tree, node.getFile())
    }

}
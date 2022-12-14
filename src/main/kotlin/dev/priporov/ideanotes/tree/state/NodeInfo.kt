package dev.priporov.ideanotes.tree.state

import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.FileNodeUtils

class NodeInfo(
    var name: String,
    var extension: String,
    var id: String? = FileNodeUtils.generateNodeName(name),
    var type:NodeType
) {
    constructor(node: FileTreeNode) : this(node.name!!, node.extension!!, node.id, node.type!!)
}

package dev.priporov.ideanotes.dto

import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.node.FileTreeNode

open class NodeStateInfo(
    var name: String?,
    var extension: String?,
    var id: String?,
    var type: NodeType?
) {
    constructor(node: FileTreeNode) : this(node.name, node.extension, node.id, node.type)
    constructor() : this(null, null, null, null)
}
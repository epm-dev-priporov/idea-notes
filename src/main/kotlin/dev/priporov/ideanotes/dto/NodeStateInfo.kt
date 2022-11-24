package dev.priporov.ideanotes.dto

import dev.priporov.ideanotes.tree.node.FileTreeNode

open class NodeStateInfo(
    var name: String?,
    var extension: String?,
    var id: String?
) {
    constructor(node: FileTreeNode) : this(node.name, node.extension, node.id)
    constructor() : this(null, null, null)
}
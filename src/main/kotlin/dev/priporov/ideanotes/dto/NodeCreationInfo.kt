package dev.priporov.ideanotes.dto

import dev.priporov.ideanotes.tree.node.FileTreeNode

class NodeCreationInfo(
    val targetNode: FileTreeNode,
    val name: String,
    val extension: String
)

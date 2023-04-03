package dev.priporov.ideanotes.dto

import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.io.File

class NodeSoftLinkCreationInfo(
    val targetNode: FileTreeNode,
    val targetFile: File
){
    val type: NodeType = NodeType.SOFT_LINK
}

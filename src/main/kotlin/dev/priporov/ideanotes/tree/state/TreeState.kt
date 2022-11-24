package dev.priporov.ideanotes.tree.state

import dev.priporov.ideanotes.dto.NodeStateInfo
import dev.priporov.ideanotes.tree.node.FileTreeNode

class TreeState {

    var order = HashMap<String?, List<String?>>()
    var nodes = HashMap<String, NodeStateInfo>()

    fun saveOrder(parent: FileTreeNode) {
        order[parent.id] = parent.children()
            .asSequence()
            .mapNotNull { it as FileTreeNode }
            .map { it.id }
            .distinct()
            .toList()
    }

    fun saveNode(node: FileTreeNode) {
        nodes[node.id!!] = NodeStateInfo(node)
    }

    fun removeNodeInfo(node: FileTreeNode) {
        nodes.remove(node.id!!)
    }

}
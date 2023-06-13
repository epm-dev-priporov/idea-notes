package dev.priporov.ideanotes.tree.state

import dev.priporov.ideanotes.dto.NodeStateInfo
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.util.concurrent.ConcurrentHashMap

class TreeState {

    var order = ConcurrentHashMap<String?, List<String?>>()
    var nodes = ConcurrentHashMap<String, NodeStateInfo>()

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
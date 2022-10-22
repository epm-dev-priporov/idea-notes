package dev.priporov.ideanotes.tree.state

import dev.priporov.ideanotes.tree.node.FileTreeNode

class TreeState {

    var order = HashMap<String?, List<String?>>()
    var nodes = HashMap<String, FileTreeNode>()

    fun saveOrder(parent: FileTreeNode) {
        order[parent.id] = parent.children()
            .asSequence()
            .mapNotNull { it as FileTreeNode }
            .map { it.id }
            .distinct()
            .toList()
    }

    fun saveNodeInfo(node: FileTreeNode) {
        nodes[node.id!!] = node
    }

    fun removeNodeInfo(node: FileTreeNode) {
        nodes.remove(node.id!!)
    }

}
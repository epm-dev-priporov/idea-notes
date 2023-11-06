package dev.priporov.ideanotes.tree.state

import dev.priporov.ideanotes.dto.NodeStateInfo
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.util.concurrent.ConcurrentHashMap

class TreeState {

    var order = ConcurrentHashMap<String?, List<String?>>()
    var nodes = ConcurrentHashMap<String, NodeStateInfo>()
    private var formatReader = ConcurrentHashMap<NodeType, String>()

    fun setReader(type:NodeType, reader:String) {
        formatReader[type] = reader
    }

    fun getReaderType(type:NodeType) = formatReader[type]

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
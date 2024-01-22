package dev.priporov.ideanotes.tree.state

import com.intellij.openapi.components.service
import dev.priporov.ideanotes.dto.NodeStateInfo
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.exporting.ExportService
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.util.concurrent.ConcurrentHashMap

class TreeState {

    private var order = ConcurrentHashMap<String?, List<String?>>()
    private var nodes = ConcurrentHashMap<String, NodeStateInfo>()

    fun getNodes() = nodes
    fun getOrder() = order
    fun getOrderByParentId(id: String) = order[id]

    fun saveOrder(parent: FileTreeNode) {
        order[parent.id] = parent.children()
            .asSequence()
            .mapNotNull { it as FileTreeNode }
            .map { it.id }
            .distinct()
            .toList()
        service<ExportService>().saveStateToJsonFile(this)
    }

    fun saveNode(node: FileTreeNode) {
        nodes[node.id!!] = NodeStateInfo(node)
        service<ExportService>().saveStateToJsonFile(this)
    }

    fun removeNodeInfo(node: FileTreeNode) {
        nodes.remove(node.id!!)
    }

}
package dev.priporov.ideanotes.tree.state

import com.intellij.openapi.components.service
import dev.priporov.ideanotes.dto.NodeStateInfo
import dev.priporov.ideanotes.tree.exporting.ExportService
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.util.concurrent.ConcurrentHashMap

class TreeState {

    private var order = ConcurrentHashMap<String?, MutableList<String?>>()
    private var nodes = ConcurrentHashMap<String, NodeStateInfo>()

    fun getNodes() = nodes
    fun getOrder() = order
    fun getOrderByParentId(id: String) = order[id]

    fun saveOrder(parent: FileTreeNode) {
        saveOrderWithoutSavingState(parent)
        service<ExportService>().saveStateToJsonFile(this)
    }

    fun saveOrderWithoutSavingState(parent: FileTreeNode) {
        val children = parent.children()
            .asSequence()
            .mapNotNull { it as FileTreeNode }
            .map { it.id }
            .distinct()
            .toMutableList()

        if(children.isEmpty()){
            order.remove(parent.id)
        } else {
            order[parent.id] = children
        }
    }

    fun saveNode(node: FileTreeNode) {
        saveNodeWithoutSavingState(node)
        service<ExportService>().saveStateToJsonFile(this)
    }

    fun saveNodeWithoutSavingState(node: FileTreeNode) {
        nodes[node.id!!] = NodeStateInfo(node)
    }

    fun removeNodeInfo(node: FileTreeNode) {
        nodes.remove(node.id!!)
    }

}
package dev.priporov.ideanotes.tree.container

import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.tree.node.NoteNode

@Service
class NoteNodeContainer {

    private val nodesGroupedById: MutableMap<String, NoteNode> = HashMap()

    fun registerNode(node: NoteNode) {
        nodesGroupedById[node.id!!] = node
    }
    fun removeNode(id: String) {
        nodesGroupedById.remove(id)
    }

    fun getNodeById(id: String): NoteNode? = nodesGroupedById[id]

}
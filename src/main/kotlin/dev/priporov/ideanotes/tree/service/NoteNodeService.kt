package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.tree.node.NoteNode

@Service
class NoteNodeService {

    private val nodesGroupedById: MutableMap<String, NoteNode> = HashMap()

    fun registerNode(node: NoteNode) {
        nodesGroupedById[node.id!!] = node
    }

    fun getNodeById(id: String): NoteNode? = nodesGroupedById[id]

}
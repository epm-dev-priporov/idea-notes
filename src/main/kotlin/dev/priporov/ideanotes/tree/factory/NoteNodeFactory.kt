package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.container.NoteNodeContainer

@Service
class NoteNodeFactory {

    fun getNode(name: String, id: String): NoteNode {
        return NoteNode(name, id).apply {
            service<NoteNodeContainer>().registerNode(this)
        }
    }

}
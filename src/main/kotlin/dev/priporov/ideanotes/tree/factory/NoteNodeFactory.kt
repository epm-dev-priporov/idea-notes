package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.container.NoteNodeContainer
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.service.FileNodeService
import java.util.*

@Service
class NoteNodeFactory {

    fun getNode(name: String, id: String? = generateNodeId(name)): NoteNode {
        return NoteNode(name, id).apply {
            service<NoteNodeContainer>().registerNode(this)
        }
    }

    fun copy(node: NoteNode): NoteNode {
        return getNode(node.name!!, node.id!!).apply {
            this.type = node.type
            this.file = node.file
        }
    }

    fun rename(name: String, node: NoteNode): NoteNode {
        val nodeContainer = service<NoteNodeContainer>()
        nodeContainer.removeNode(node.id!!)
        return node.apply {
            this.name = name
            this.userObject = name
            this.id = generateNodeId(name)
            if (this.file != null) {
                service<FileNodeService>().renameFile(this.file!!, this)
            }
            nodeContainer.registerNode(this)
        }
    }

    private fun generateNodeId(name: String?): String? {
        if (name == null) {
            return null
        }
        return "${name}_${UUID.randomUUID().toString().substring(0, 6)}"
    }

}
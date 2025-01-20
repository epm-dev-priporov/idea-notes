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
        val fileNodeService = service<FileNodeService>()
        return getNode(node.name!!).apply {
            this.type = node.type

//            this.file = fileNodeService.createApplicationFile(
//                this.id!!,
//                this.type.extension!!,
//                null
//            )

        }

    }

    private fun generateNodeId(name: String?): String? {
        if (name == null) {
            return null
        }
        return "${name}_${UUID.randomUUID().toString().substring(0, 6)}"
    }

}
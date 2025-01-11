package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto
import dev.priporov.ideanotes.tree.node.dto.NodeDefinitionDto
import java.util.*

@Service
class CreateNodeDtoFactory {

    fun toCreateNodeDto(
        name: String,
        definition: NodeDefinitionDto,
        content: ByteArray? = null
    ) = CreateNodeDto().apply {
        this.name = name
        this.type = definition.type
        this.id = generateNodeId(name)
        this.content = content
    }

    private fun generateNodeId(name: String?): String? {
        if (name == null) {
            return null
        }
        return "${name}_${UUID.randomUUID().toString().substring(0, 6)}"
    }

}
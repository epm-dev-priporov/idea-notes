package dev.priporov.ideanotes.tree.node.mapper

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.factory.NoteNodeFactory
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto

@Service
class CreateDtoToTreeNodeMapper {

    fun toNoteNode(createNodeDto: CreateNodeDto): NoteNode {
        return service<NoteNodeFactory>().getNode(createNodeDto.name!!).apply {
            type = createNodeDto.type
        }
    }

}
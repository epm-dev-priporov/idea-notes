package dev.priporov.ideanotes.state

import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.StateNodeDto
import java.util.concurrent.ConcurrentHashMap


class TreeStateDto {

    fun insertInto(stateNodeDto: StateNodeDto, parentNode: NoteNode) {
        nodesGroupedById[stateNodeDto.id] = stateNodeDto

        hierarchy.computeIfAbsent(parentNode.id!!, { ArrayList() }).add(stateNodeDto.id)
    }

    var hierarchy = ConcurrentHashMap<String, MutableList<String>>()
    var nodesGroupedById = ConcurrentHashMap<String, StateNodeDto>()

}
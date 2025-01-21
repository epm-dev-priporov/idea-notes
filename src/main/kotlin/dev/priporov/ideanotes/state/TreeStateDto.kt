package dev.priporov.ideanotes.state

import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.StateNodeDto
import java.util.concurrent.ConcurrentHashMap


class TreeStateDto {

    fun insertInto(stateNodeDto: StateNodeDto, parentNode: NoteNode) {
        nodesGroupedById[stateNodeDto.id!!] = stateNodeDto

        hierarchy.computeIfAbsent(parentNode.id!!, { ArrayList() }).add(stateNodeDto.id!!)
    }

    fun renameNode(oldId: String, newId: String, name: String) {
        nodesGroupedById[newId] = nodesGroupedById[oldId]!!.apply {
            this.id = newId
            this.name = name
        }
        nodesGroupedById.remove(oldId)
        val oldNodeChildren = hierarchy[oldId]
        if (oldNodeChildren != null) {
            hierarchy[newId] = oldNodeChildren
        }
        hierarchy.remove(oldId)

        hierarchy.forEach { id, children ->
            for ((index, childId) in children.withIndex()) {
                if (childId == oldId) {
                    children.removeAt(index)
                    children.add(index, newId)
                }
            }
        }

    }

    var hierarchy = ConcurrentHashMap<String, MutableList<String>>()
    var nodesGroupedById = ConcurrentHashMap<String, StateNodeDto>()

}
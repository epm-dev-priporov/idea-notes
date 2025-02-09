package dev.priporov.ideanotes.tree.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.StateNodeDto
import java.io.File

abstract class BaseTreeStateService {

    protected open lateinit var treeState: TreeStateDto

    protected val mapper = ObjectMapper()

    open fun updateNodeOrder(node: NoteNode) {
        val children: MutableList<String> = node.children()
            .asSequence()
            .mapNotNull { it as NoteNode }
            .map { it.id }
            .filterNotNull()
            .distinct()
            .toMutableList()

        if (children.isEmpty()) {
            treeState.hierarchy.remove(node.id)
        } else {
            treeState.hierarchy[node.id!!] = children
        }
        saveStateFile(treeState)
    }

    open fun getStateTree() = treeState

    open fun rename(oldId: String, newId: String, name: String) {}

    fun insertInto(node: NoteNode, parentNode: NoteNode) {
        val stateNodeDto = StateNodeDto().apply {
            id = node.id
            type = node.type
            name = node.name
        }
        treeState.insertInto(stateNodeDto, parentNode)
        saveStateFile(treeState)
    }

    protected fun readTreeState(): TreeStateDto {
        val stateFile = File(getStateFilePath())
        if (!stateFile.exists()) {
            stateFile.createNewFile()
            return TreeStateDto()
        }
        if (stateFile.length() > 0) {
            return mapper.readValue<TreeStateDto>(stateFile)
        }
        return TreeStateDto()
    }

    protected fun saveStateFile(treeState: TreeStateDto) {
        File(getStateFilePath()).writeText(mapper.writeValueAsString(treeState))
    }

    protected abstract fun getStateFilePath(): String

}
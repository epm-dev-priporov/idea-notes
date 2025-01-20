package dev.priporov.ideanotes.tree.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.state.BaseTreeState
import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.StateNodeDto
import java.io.File
import java.util.*

const val stateFileName = ".state.json"

@Service
class ApplicationTreeStateService : BaseTreeState() {
    private val stateFilePath = getStateFilePath()
    private val mapper = ObjectMapper()

    lateinit var treeState: TreeStateDto

    fun init() {
        treeState = readTreeState()
    }

    fun delete(id: String, parentId: String?) {
        treeState.hierarchy[parentId]?.remove(id)
        treeState.hierarchy.remove(id)
        treeState.nodesGroupedById.remove(id)
        saveStateFile(treeState)
    }

    fun insertInto(node: NoteNode, parentNode: NoteNode) {
        val stateNodeDto = StateNodeDto().apply {
            id = node.id
            type = node.type
            name = node.name
        }
        treeState.insertInto(stateNodeDto, parentNode)
        saveStateFile(treeState)
    }

    fun getChildrenRecursively(parentId: String): List<String> {
        val result = ArrayList<String>()
        val queue: Deque<String> = LinkedList(treeState.hierarchy[parentId] ?: emptyList())

        while (queue.isNotEmpty()) {
            val id = queue.pop()
            result.add(id)
            queue.addAll(treeState.hierarchy[id] ?: emptyList())
        }
        return result
    }

    override fun saveStateFile(treeState: TreeStateDto) {
        File(stateFilePath).writeText(mapper.writeValueAsString(treeState))
    }

    private fun getStateFilePath(): String {
        val applicationBaseDir = service<ApplicationStateService>().getApplicationBaseDIr()
        return "$applicationBaseDir$fileSeparator$stateFileName"
    }

    private fun readTreeState(): TreeStateDto {
        val stateFile = File(stateFilePath)
        if (!stateFile.exists()) {
            stateFile.createNewFile()
            return TreeStateDto()
        }
        if (stateFile.length() > 0) {
            return mapper.readValue<TreeStateDto>(stateFile)
        }
        return TreeStateDto()
    }
}

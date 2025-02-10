package dev.priporov.ideanotes.tree.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.StateNodeDto
import java.io.File
import java.util.*

const val stateFileName = "state.json"

@Service
class ApplicationTreeStateService : BaseTreeStateService() {

    fun init() {
        treeState = readTreeState()
    }

    override fun rename(oldId: String, newId: String, name: String) {
        treeState.renameNode(oldId, newId, name)
        saveStateFile(treeState)
    }

    fun delete(id: String, parentId: String?) {
        treeState.hierarchy[parentId]?.remove(id)
        treeState.hierarchy.remove(id)
        treeState.nodesGroupedById.remove(id)
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

    override fun getStateFilePath(): String {
        val applicationBaseDir = service<PluginStateService>().getApplicationBaseDIr()
        return "$applicationBaseDir$fileSeparator$stateFileName"
    }

}

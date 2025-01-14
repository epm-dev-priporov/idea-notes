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

const val stateFileName = "state.json"

@Service
class ApplicationTreeStateService : BaseTreeState() {
    private val stateFilePath = getStateFilePath()
    private val mapper = ObjectMapper()

    var treeState: TreeStateDto = init()

    fun init(): TreeStateDto {
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

    fun insertInto(node: NoteNode, parentNode: NoteNode) {
        val stateNodeDto = StateNodeDto(node.id!!).apply {
            type = node.type
            name = node.name
        }
        treeState.insertInto(stateNodeDto, parentNode)
        saveStateFile(treeState)
    }

    override fun saveStateFile(treeState: TreeStateDto) {
        File(stateFilePath).writeText(mapper.writeValueAsString(treeState))
    }

    private fun getStateFilePath(): String {
        val applicationBaseDir = service<ApplicationStateService>().getApplicationBaseDIr()
        return "$applicationBaseDir$fileSeparator$stateFileName"
    }

}
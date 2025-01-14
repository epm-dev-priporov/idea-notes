package dev.priporov.ideanotes.state

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.StateNodeDto
import dev.priporov.ideanotes.tree.node.fileSeparator
import java.io.File


private const val stateFileName = "state.json"

@Service
class ApplicationTreeStateService:BaseTreeState() {
    private val stateFilePath = getStateFilePath()
    private val mapper = ObjectMapper()

    var treeState: TreeStateDto = init()

    private fun init(): TreeStateDto {
        val stateFile = File(stateFilePath)
        if (!stateFile.exists()) {
            stateFile.createNewFile()
            return TreeStateDto()
        }

        return mapper.readValue<TreeStateDto>(stateFile)
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

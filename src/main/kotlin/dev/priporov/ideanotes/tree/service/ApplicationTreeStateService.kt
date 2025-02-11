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

    override fun getStateFilePath(): String {
        val applicationBaseDir = service<PluginStateService>().getApplicationBaseDIr()
        return "$applicationBaseDir$fileSeparator$stateFileName"
    }

}

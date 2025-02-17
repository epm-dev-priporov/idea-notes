package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service

const val stateFileName = "state.json"
const val exportStateFileName = "exp_state.json"

@Service
class ApplicationTreeStateService : BaseTreeStateService() {

    fun init() {
        treeState = readTreeState()
    }

    override fun getStateFilePath(): String {
        val applicationBaseDir = service<PluginStateService>().getApplicationBaseDir()
        return "$applicationBaseDir$fileSeparator$stateFileName"
    }

}

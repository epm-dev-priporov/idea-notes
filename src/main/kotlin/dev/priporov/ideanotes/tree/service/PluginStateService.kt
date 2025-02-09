package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import dev.priporov.ideanotes.state.PluginStateDto

@State(
    name = "StateService",
    storages = [Storage("ideanotes_v2.xml")]
)
@Service
class PluginStateService : PersistentStateComponent<PluginStateDto> {

    var applicationState: PluginStateDto = PluginStateDto()

    override fun getState() = applicationState

    override fun loadState(loadedState: PluginStateDto) {
        applicationState = loadedState
    }

    fun getApplicationBaseDIr() = applicationState.appBaseDir
    fun getProjectBaseDir(project: Project): String {
        return "${project.guessProjectDir()?.path}$fileSeparator${applicationState.projectNoteDir}"
    }

}
package dev.priporov.ideanotes.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "StateService",
    storages = [Storage("ideanotes_v2.xml")]
)
@Service
class ApplicationStateService : PersistentStateComponent<ApplicationStateDto> {

    var applicationState: ApplicationStateDto = ApplicationStateDto()

    override fun getState() = applicationState

    override fun loadState(loadedState: ApplicationStateDto) {
        applicationState = loadedState
    }

    fun getApplicationBaseDIr() = applicationState.appBaseDir

}
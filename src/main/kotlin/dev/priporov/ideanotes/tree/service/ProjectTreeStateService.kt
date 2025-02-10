package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.state.TreeStateDto

@Service(Service.Level.PROJECT)
class ProjectTreeStateService(private val project: Project) : BaseTreeStateService() {

    override var treeState: TreeStateDto = readTreeState()

    override fun rename(oldId: String, newId: String, name: String) {
        treeState.renameNode(oldId, newId, name)
        saveStateFile(treeState)
    }

    override fun getStateFilePath(): String {
        val projectBaseDir = service<PluginStateService>().getProjectBaseDir(project)
        return "$projectBaseDir$fileSeparator$stateFileName"
    }

}
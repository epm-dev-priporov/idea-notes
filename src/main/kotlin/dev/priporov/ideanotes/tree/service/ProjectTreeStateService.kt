package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir

@Service(Service.Level.PROJECT)
class ProjectTreeStateService(private val project: Project?) : BaseTreeStateService() {

    override fun getStateFilePath(): String {
        val projectBaseDir = service<PluginStateService>().getProjectBaseDir(project!!)
        return "$projectBaseDir$fileSeparator$stateFileName"
    }

}
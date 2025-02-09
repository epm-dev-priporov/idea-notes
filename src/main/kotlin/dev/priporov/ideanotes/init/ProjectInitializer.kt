package dev.priporov.ideanotes.init

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import dev.priporov.ideanotes.tree.service.FileNodeService
import dev.priporov.ideanotes.tree.service.PluginStateService

class ProjectInitializer : ProjectActivity {

    override suspend fun execute(project: Project) {
        initBaseDirIfNotExists(project)
    }

    /** Init base directory of the plugin
     *
     */
    private fun initBaseDirIfNotExists(project: Project) {
        service<FileNodeService>().createBaseDirIfNotExists(
            service<PluginStateService>().getProjectBaseDir(project)
        )
    }


}

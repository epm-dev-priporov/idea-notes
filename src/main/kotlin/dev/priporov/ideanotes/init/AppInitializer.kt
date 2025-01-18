package dev.priporov.ideanotes.init

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.action.SelectFileInProjectViewAction
import dev.priporov.ideanotes.tree.node.dto.NodeDefinitionDto
import dev.priporov.ideanotes.tree.service.ApplicationStateService
import dev.priporov.ideanotes.tree.service.ApplicationTreeStateService
import dev.priporov.ideanotes.tree.service.FileNodeService
import dev.priporov.ideanotes.tree.service.NodeDefinitionService


class AppInitializer : AppLifecycleListener {
    private val mapper = ObjectMapper()

    override fun appStarted() {
        loadNodeDefinitions()
        loadTreeStateFromFile()
        initBaseDirIfNotExists()
    }

    fun method(){
//        ActionUtil.getShortcutSet("SelectInProjectView").shortcuts.also { shortcutSet ->
//            SelectFileInProjectViewAction()
//                .registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), textEditor.component)
//        }
    }

    /** load plugin global state from file

    */
    private fun loadTreeStateFromFile() {
        service<ApplicationTreeStateService>().init()
    }

    /** Init base directory of the plugin
     *
     */
    private fun initBaseDirIfNotExists() {
        service<FileNodeService>().createBaseDirIfNotExists(
            service<ApplicationStateService>().getApplicationBaseDIr()
        )
    }

    /** Load node definition from /resources/nodes/nodes.json
     *
     */
    private fun loadNodeDefinitions() {
        val inputStream = AppInitializer::class.java.getResourceAsStream("/nodes/nodes.json")
        val nodeDefinitions = mapper.readValue(inputStream, object : TypeReference<List<NodeDefinitionDto>>() {})
            .asSequence()
            .associateByTo(HashMap()) { it.type }

        service<NodeDefinitionService>().init(nodeDefinitions)
    }


}
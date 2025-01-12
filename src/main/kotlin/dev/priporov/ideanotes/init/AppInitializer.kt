package dev.priporov.ideanotes.init

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.state.StateService
import dev.priporov.ideanotes.tree.node.FileNodeService
import dev.priporov.ideanotes.tree.node.NodeDefinitionService
import dev.priporov.ideanotes.tree.node.dto.NodeDefinitionDto


class AppInitializer : AppLifecycleListener {
    private val mapper = ObjectMapper()

    override fun appStarted() {
        loadNodeDefinitions()
        initBaseDirIfNotExists()
    }

    /** Init base directory of the plugin
     *
     */
    private fun initBaseDirIfNotExists(){
        val applicationState = service<StateService>().aaplicationState
        service<FileNodeService>().createBaseDirIfNotExists(applicationState.appBaseDir)
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
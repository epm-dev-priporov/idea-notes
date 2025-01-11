package dev.priporov.ideanotes.tree.node.init

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.node.dto.NodeDefinitionDto


class NodeLoader : AppLifecycleListener {
    private val mapper = ObjectMapper()

    override fun appStarted() {
        val inputStream = NodeLoader::class.java.getResourceAsStream("/nodes/nodes.json")
        val nodeDefinitions = mapper.readValue(inputStream, object : TypeReference<List<NodeDefinitionDto>>() {})
            .asSequence()
            .associateByTo(HashMap()) { it.type }

        service<NodeDefinitionService>().init(nodeDefinitions)
    }
}
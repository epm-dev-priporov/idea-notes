package dev.priporov.ideanotes.tree.node

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.tree.node.dto.NodeDefinitionDto
import dev.priporov.ideanotes.tree.node.dto.NodeType
import java.util.*

@Service
class NodeDefinitionService {

    private lateinit var mapOfNodeDefinitions: Map<NodeType, NodeDefinitionDto>
    private lateinit var orderedNodeDefinitions: List<NodeDefinitionDto>
    private lateinit var supportedDefinitionsForCreation: List<NodeDefinitionDto>

    private val ideName = ApplicationInfo.getInstance().fullApplicationName
    private val osName = System.getProperty("os.name").lowercase(Locale.ENGLISH)

    fun init(nodeDefinitions: Map<NodeType, NodeDefinitionDto>) {
        this.mapOfNodeDefinitions = nodeDefinitions
        this.orderedNodeDefinitions = nodeDefinitions
            .values
            .asSequence()
            .filter(this::filterByOs)
            .filter(this::filterByIde)
            .sortedBy { it.index }
            .toList()

        this.supportedDefinitionsForCreation = orderedNodeDefinitions.asSequence()
            .filter { !it.ignore }
            .toList()

    }

    fun getSupportedDefinitionsForCreation() = supportedDefinitionsForCreation

    private fun filterByIde(definition: NodeDefinitionDto): Boolean {
        val ides = definition.listOfSupportedIde
        if (!ides.isNullOrEmpty()) {
            return ides.any { name -> ideName.contains(name) }
        }
        return true
    }

    private fun filterByOs(definition: NodeDefinitionDto): Boolean {
        val os = definition.os

        if (!os.isNullOrEmpty()) {
            return os.any { name -> osName.contains(name) }
        }
        return true
    }

}
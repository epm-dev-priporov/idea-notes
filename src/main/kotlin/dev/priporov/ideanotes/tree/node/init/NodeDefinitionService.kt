package dev.priporov.ideanotes.tree.node.init

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.Service
import java.util.*

@Service
class NodeDefinitionService {

    lateinit var mapOfNodeDefinitions: Map<NodeType, NodeDefinition>
    lateinit var orderedNodeDefinitions: List<NodeDefinition>

    private val ideName = ApplicationInfo.getInstance().fullApplicationName
    private val osName = System.getProperty("os.name").lowercase(Locale.ENGLISH)

    fun init(nodeDefinitions: Map<NodeType, NodeDefinition>) {
        this.mapOfNodeDefinitions = nodeDefinitions
        this.orderedNodeDefinitions = nodeDefinitions
            .values
            .asSequence()
            .filter(this::filterByOs)
            .filter(this::filterByIde)
            .sortedBy { it.index }
            .toList()
    }

    private fun filterByIde(definition: NodeDefinition): Boolean {
        val ides = definition.listOfSUpportedIde
        if (!ides.isNullOrEmpty()) {
            return ides.any { name -> ideName.contains(name) }
        }
        return true
    }

    private fun filterByOs(definition: NodeDefinition): Boolean {
        val os = definition.os

        if (!os.isNullOrEmpty()) {
            return os.any { name -> osName.contains(name) }
        }
        return true
    }
}
package dev.priporov.ideanotes.tree.node.init

import com.intellij.openapi.components.Service

@Service
class NodeDefinitionService {

    lateinit var mapOfNodeDefinitions: HashMap<NodeType, NodeDefinition>
    lateinit var orderedNodeDefinitions: List<NodeDefinition>

    fun init(nodeDefinitions: java.util.HashMap<NodeType, NodeDefinition>) {
        this.mapOfNodeDefinitions = nodeDefinitions
        this.orderedNodeDefinitions = nodeDefinitions.values.sortedBy { it.index }
    }

}
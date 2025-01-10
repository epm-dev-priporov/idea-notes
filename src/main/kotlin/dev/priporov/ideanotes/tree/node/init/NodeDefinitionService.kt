package dev.priporov.ideanotes.tree.node.init

import com.intellij.openapi.components.Service

@Service
class NodeDefinitionService {

    lateinit var nodeDefinitions: HashMap<NodeType, NodeDefinition>

    fun init(nodeDefinitions: java.util.HashMap<NodeType, NodeDefinition>) {
        this.nodeDefinitions = nodeDefinitions
    }

}
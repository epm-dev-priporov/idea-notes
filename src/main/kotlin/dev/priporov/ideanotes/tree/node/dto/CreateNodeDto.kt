package dev.priporov.ideanotes.tree.node.dto

class CreateNodeDto {
    var id: String? = null
    var name: String? = null
    var type: NodeType = NodeType.UNKNOWN
    var content:ByteArray? = null
}
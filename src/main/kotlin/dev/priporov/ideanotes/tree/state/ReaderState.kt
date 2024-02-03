package dev.priporov.ideanotes.tree.state

import dev.priporov.ideanotes.dto.NodeStateInfo
import dev.priporov.ideanotes.tree.common.NodeType
import java.util.concurrent.ConcurrentHashMap

class ReaderState {

    private var formatReader = ConcurrentHashMap<NodeType, String>()
    var order = ConcurrentHashMap<String?, MutableList<String?>>()
    var nodes = ConcurrentHashMap<String, NodeStateInfo>()
    var isImported:Boolean = false

    fun setReader(type: NodeType, reader: String) {
        formatReader[type] = reader
    }

    fun getReaderType(type: NodeType) = formatReader[type]

}
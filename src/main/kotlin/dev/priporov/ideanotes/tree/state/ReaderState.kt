package dev.priporov.ideanotes.tree.state

import dev.priporov.ideanotes.tree.common.NodeType
import java.util.concurrent.ConcurrentHashMap

class ReaderState {

    private var formatReader = ConcurrentHashMap<NodeType, String>()

    fun setReader(type: NodeType, reader: String) {
        formatReader[type] = reader
    }

    fun getReaderType(type: NodeType) = formatReader[type]

}
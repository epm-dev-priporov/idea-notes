package dev.priporov.ideanotes.dto

import dev.priporov.ideanotes.tree.state.NodeInfo

class NodeCopyData(
    val nodeInfo: NodeInfo,
    val content: ByteArray,
) {

}
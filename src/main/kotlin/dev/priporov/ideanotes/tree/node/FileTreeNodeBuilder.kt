package dev.priporov.ideanotes.tree.node

import com.intellij.openapi.components.Service

@Service
class FileTreeNodeBuilder {

    fun build(): FileTreeNode {
        val node = FileTreeNode()

        return node
    }

}
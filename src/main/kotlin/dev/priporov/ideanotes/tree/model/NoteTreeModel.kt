package dev.priporov.ideanotes.tree.model

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.node.RootNode
import javax.swing.tree.DefaultTreeModel

@Service
class NoteTreeModel(private val rootNode: RootNode = service<RootNode>()) : DefaultTreeModel(rootNode) {

    override fun getRoot(): RootNode {
        return rootNode
    }

}
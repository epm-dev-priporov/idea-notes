package dev.priporov.ideanotes.tree.model

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.node.AppRootNode

@Service
class AppNoteTreeModel(private val rootNode: AppRootNode = service<AppRootNode>()) : BaseNoteTreeModel(rootNode) {

}
package dev.priporov.ideanotes.tree.node

import com.intellij.openapi.components.Service

const val rootName = "root"

@Service
class AppRootNode(override var id: String? = rootName) : NoteNode(rootName) {

}
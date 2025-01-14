package dev.priporov.ideanotes.tree.node

import com.intellij.openapi.components.Service

const val name = "root"

@Service
class AppRootNode(override var id: String? = name) : NoteNode(name) {

}
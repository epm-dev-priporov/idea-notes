package dev.priporov.ideanotes.tree.node

import com.intellij.openapi.components.Service

@Service(Service.Level.PROJECT)
class ProjectRootNode(override var id: String? = rootName) : NoteNode(rootName)
package dev.priporov.ideanotes.tree.node

import com.intellij.openapi.components.Service

const val projectRootName = "projectRootName"

@Service(Service.Level.PROJECT)
class ProjectRootNode : FileTreeNode(projectRootName)
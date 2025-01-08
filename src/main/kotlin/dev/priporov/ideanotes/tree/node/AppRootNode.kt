package dev.priporov.ideanotes.tree.node

import com.intellij.openapi.components.Service

const val name = "root"

@Service
class AppRootNode : FileTreeNode(name)
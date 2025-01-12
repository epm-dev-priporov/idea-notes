package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.node.TestNoteNode


@Service(Service.Level.PROJECT)
class AppNoteTree : BaseTree<AppNoteTreeModel>() {

}
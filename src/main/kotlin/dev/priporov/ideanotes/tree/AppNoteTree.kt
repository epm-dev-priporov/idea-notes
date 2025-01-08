package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.node.FileTreeNode


@Service(Service.Level.PROJECT)
class AppNoteTree : BaseTree<AppNoteTreeModel>() {

    init {
        setModel(service<AppNoteTreeModel>())
        model = service<AppNoteTreeModel>()
        isRootVisible = false

        // TODO
        getTreeModel().root.insert(FileTreeNode("test"), 0)
        getTreeModel().root.insert(FileTreeNode("test2"), 1)
        getTreeModel().root.insert(FileTreeNode("test3"), 1)

        expandAll()
    }

}
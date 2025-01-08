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
        val root = getTreeModel().root
        root.insert(FileTreeNode("test"), 0)

        val firstChild = root.firstChild as FileTreeNode
        firstChild.insert(FileTreeNode("test2"), 0)
        firstChild.insert(FileTreeNode("test2"), 0)
        root.insert(FileTreeNode("test2"), 1)
        root.insert(FileTreeNode("test3"), 1)

        expandAll()
    }

}
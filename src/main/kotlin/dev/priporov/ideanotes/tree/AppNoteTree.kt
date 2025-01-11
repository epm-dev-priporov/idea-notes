package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.node.TestFileTreeNode


@Service(Service.Level.PROJECT)
class AppNoteTree : BaseTree<AppNoteTreeModel>() {

    init {
        // TODO
        val root = getTreeModel().root
        root.insert(TestFileTreeNode("test"), 0)

        val firstChild = root.firstChild as TestFileTreeNode
        firstChild.insert(TestFileTreeNode("test2"), 0)
        firstChild.insert(TestFileTreeNode("test2"), 0)
        root.insert(TestFileTreeNode("test2"), 1)
        root.insert(TestFileTreeNode("test3"), 1)

        expandAll()
    }

}
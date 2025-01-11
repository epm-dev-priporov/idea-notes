package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.node.TestNoteNode


@Service(Service.Level.PROJECT)
class AppNoteTree : BaseTree<AppNoteTreeModel>() {

    init {
        // TODO
        val root = getTreeModel().root
        root.insert(TestNoteNode("test"), 0)

        val firstChild = root.firstChild as TestNoteNode
        firstChild.insert(TestNoteNode("test2"), 0)
        firstChild.insert(TestNoteNode("test2"), 0)
        root.insert(TestNoteNode("test2"), 1)
        root.insert(TestNoteNode("test3"), 1)

        expandAll()
    }

}
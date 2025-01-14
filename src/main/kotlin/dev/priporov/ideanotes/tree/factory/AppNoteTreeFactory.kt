package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.AppNoteTree
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.model.NoteTreeCellRenderer
import dev.priporov.ideanotes.tree.node.TestNoteNode


@Service
class AppNoteTreeFactory() : BaseNoteTreeFactory<AppNoteTree>() {

    fun getInstance(project: Project): AppNoteTree {
        val tree = project.getService(AppNoteTree::class.java).apply {
            setModel(service<AppNoteTreeModel>())
            model = service<AppNoteTreeModel>()
            cellRenderer = service<NoteTreeCellRenderer>()
            isRootVisible = false
        }

        tree.apply {
            // TODO
            val root = getTreeModel().root
            root.insert(TestNoteNode("test"), 0)

            val firstChild = root.firstChild as TestNoteNode
            firstChild.insert(TestNoteNode("test2"), 0)
            firstChild.insert(TestNoteNode("test2"), 0)
            root.insert(TestNoteNode("test2"), 1)
            root.insert(TestNoteNode("test3"), 1)
        }

        init(tree)

        return tree
    }

}
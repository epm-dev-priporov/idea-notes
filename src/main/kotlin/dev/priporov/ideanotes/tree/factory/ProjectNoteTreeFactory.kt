package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.ProjectNoteTree
import dev.priporov.ideanotes.tree.model.NoteTreeCellRenderer
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel
import dev.priporov.ideanotes.tree.node.TestNoteNode


@Service
class ProjectNoteTreeFactory() : BaseNoteTreeFactory<ProjectNoteTree>() {

    fun getInstance(project: Project): ProjectNoteTree {
        val tree = project.getService(ProjectNoteTree::class.java).apply {
            setModel(project.getService(ProjectNoteTreeModel::class.java))
            model = project.getService(ProjectNoteTreeModel::class.java)
            cellRenderer = service<NoteTreeCellRenderer>()
            isRootVisible = false
        }

        tree.apply {
            val root = getTreeModel().root
            root.insert(TestNoteNode("test"), 0)

            val firstChild = root.firstChild as TestNoteNode
            firstChild.insert(TestNoteNode("test2"), 0)
            firstChild.insert(TestNoteNode("test2"), 0)

            getTreeModel().root.insert(TestNoteNode("project test"), 0)
            getTreeModel().root.insert(TestNoteNode("project test2"), 1)
            getTreeModel().root.insert(TestNoteNode("project test3"), 1)
        }

        init(tree)

        return tree
    }

}
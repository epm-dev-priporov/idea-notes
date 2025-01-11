package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel
import dev.priporov.ideanotes.tree.node.TestNoteNode


@Service(Service.Level.PROJECT)
class ProjectNoteTree(project: Project) : BaseTree<ProjectNoteTreeModel>() {

    init {
        // TODO
        val root = getTreeModel().root
        root.insert(TestNoteNode("test"), 0)

        val firstChild = root.firstChild as TestNoteNode
        firstChild.insert(TestNoteNode("test2"), 0)
        firstChild.insert(TestNoteNode("test2"), 0)

        getTreeModel().root.insert(TestNoteNode("project test"), 0)
        getTreeModel().root.insert(TestNoteNode("project test2"), 1)
        getTreeModel().root.insert(TestNoteNode("project test3"), 1)

        expandAll()
    }

}
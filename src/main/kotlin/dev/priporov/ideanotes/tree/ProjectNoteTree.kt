package dev.priporov.ideanotes.tree

import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel
import dev.priporov.ideanotes.tree.node.TestFileTreeNode


class ProjectNoteTree(project: Project) : BaseTree<ProjectNoteTreeModel>() {

    init {
        setModel(project.getService(ProjectNoteTreeModel::class.java))
        model = project.getService(ProjectNoteTreeModel::class.java)
        isRootVisible = false

        // TODO
        val root = getTreeModel().root
        root.insert(TestFileTreeNode("test"), 0)

        val firstChild = root.firstChild as TestFileTreeNode
        firstChild.insert(TestFileTreeNode("test2"), 0)
        firstChild.insert(TestFileTreeNode("test2"), 0)

        getTreeModel().root.insert(TestFileTreeNode("project test"), 0)
        getTreeModel().root.insert(TestFileTreeNode("project test2"), 1)
        getTreeModel().root.insert(TestFileTreeNode("project test3"), 1)

        expandAll()
    }

}
package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel
import dev.priporov.ideanotes.tree.node.FileTreeNode

@Service(Service.Level.PROJECT)
class ProjectNoteTree(project: Project) : BaseTree<ProjectNoteTreeModel>() {

    init {
        setModel(project.getService(ProjectNoteTreeModel::class.java))
        model = project.getService(ProjectNoteTreeModel::class.java)
        isRootVisible = false

        // TODO
        getTreeModel().root.insert(FileTreeNode("project test"), 0)
        getTreeModel().root.insert(FileTreeNode("project test2"), 1)
        getTreeModel().root.insert(FileTreeNode("project test3"), 1)

        expandAll()
    }

}
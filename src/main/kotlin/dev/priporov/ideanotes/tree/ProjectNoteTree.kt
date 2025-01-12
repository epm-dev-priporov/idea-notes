package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel
import dev.priporov.ideanotes.tree.node.TestNoteNode


@Service(Service.Level.PROJECT)
class ProjectNoteTree(project: Project) : BaseTree<ProjectNoteTreeModel>() {

}
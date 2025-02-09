package dev.priporov.ideanotes.tree.model

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.node.ProjectRootNode

@Service(Service.Level.PROJECT)
class ProjectNoteTreeModel(private val project: Project) : BaseNoteTreeModel(project.getService(ProjectRootNode::class.java)) {

}
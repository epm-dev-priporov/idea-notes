package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.ProjectNoteTree

@Service
class ProjectNoteTreeFactory() : BaseNoteTreeFactory<ProjectNoteTree>() {

    fun getInstance(project: Project): ProjectNoteTree {
        val tree = project.getService(ProjectNoteTree::class.java)
        init(tree)

        return tree
    }

}
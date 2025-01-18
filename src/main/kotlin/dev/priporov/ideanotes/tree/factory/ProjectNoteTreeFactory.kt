package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.ProjectNoteTree
import dev.priporov.ideanotes.tree.model.NoteTreeCellRenderer
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel


@Service
class ProjectNoteTreeFactory() : BaseNoteTreeFactory<ProjectNoteTree>() {

    fun getInstance(project: Project): ProjectNoteTree {
        val tree = project.getService(ProjectNoteTree::class.java).apply {
            setModel(project.getService(ProjectNoteTreeModel::class.java))
            model = project.getService(ProjectNoteTreeModel::class.java)
            cellRenderer = service<NoteTreeCellRenderer>()
            isRootVisible = false
        }

        init(tree)

        return tree
    }

}
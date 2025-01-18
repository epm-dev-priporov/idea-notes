package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.AppNoteTree
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.model.NoteTreeCellRenderer


@Service
class AppNoteTreeFactory() : BaseNoteTreeFactory<AppNoteTree>() {

    fun getInstance(project: Project): AppNoteTree {
        val tree = project.getService(AppNoteTree::class.java).apply {
            setModel(service<AppNoteTreeModel>())
            model = service<AppNoteTreeModel>()
            cellRenderer = service<NoteTreeCellRenderer>()
            isRootVisible = false
        }

        init(tree)

        return tree
    }

}
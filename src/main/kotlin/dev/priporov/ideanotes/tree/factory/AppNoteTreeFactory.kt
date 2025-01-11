package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.AppNoteTree
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel


@Service
class AppNoteTreeFactory() : BaseNoteTreeFactory<AppNoteTree>() {

    fun getInstance(project: Project): AppNoteTree {
        val tree = project.getService(AppNoteTree::class.java).apply {
            setModel(service<AppNoteTreeModel>())
            model = service<AppNoteTreeModel>()
            isRootVisible = false
        }

        init(tree)

        return tree
    }

}
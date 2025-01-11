package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.AppNoteTree


class AppNoteTreeFactory():BaseNoteTreeFactory<AppNoteTree>() {

    fun getInstance(project: Project): AppNoteTree {
//        val tree = project.getService(AppNoteTree::class.java)
        val tree = AppNoteTree()
        init(tree)

        return tree
    }

}
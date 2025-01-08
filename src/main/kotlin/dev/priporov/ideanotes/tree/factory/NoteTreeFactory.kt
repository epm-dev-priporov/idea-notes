package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.AppNoteTree

@Service
class NoteTreeFactory {

    fun getAppInstance(project: Project): AppNoteTree {
        val tree = project.getService(AppNoteTree::class.java)

        return tree
    }

}
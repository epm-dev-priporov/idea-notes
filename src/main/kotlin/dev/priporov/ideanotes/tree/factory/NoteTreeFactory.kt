package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.NoteTree

@Service
class NoteTreeFactory {

    fun getInstance(project: Project): NoteTree {
        val tree = project.getService(NoteTree::class.java)

        return tree
    }

}
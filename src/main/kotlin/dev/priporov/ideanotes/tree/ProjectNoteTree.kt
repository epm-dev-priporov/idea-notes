package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto


@Service(Service.Level.PROJECT)
class ProjectNoteTree(val project: Project) : BaseTree<ProjectNoteTreeModel>() {

    override fun createNewInRoot(createNodeDto: CreateNodeDto): NoteNode {
        val node = super.createInto(createNodeDto, getRoot())

        return node
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }

}
package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto
import dev.priporov.ideanotes.tree.node.dto.NodeType
import dev.priporov.ideanotes.tree.service.FileNodeService
import dev.priporov.ideanotes.tree.service.ProjectTreeStateService

@Service(Service.Level.PROJECT)
class ProjectNoteTree(val project: Project) : BaseTree<ProjectNoteTreeModel>() {

    override fun renameNode(name: String, node: NoteNode) {
        val oldId = node.id!!
        super.renameNode(name, node)
        val newId = node.id!!

        project.service<ProjectTreeStateService>().rename(oldId, newId, name)

        val expandedNodes = getExpandedNodes(node)
        getTreeModel().reload(node.parent)
        expandNodes(expandedNodes)
    }

    override fun createInto(createNodeDto: CreateNodeDto, targetNode: NoteNode): NoteNode {
        val node = super.createInto(createNodeDto, targetNode)

        if (node.type != NodeType.PACKAGE && node.type != NodeType.UNKNOWN) {
            val extension = node.type.extension
            val virtualFile: VirtualFile = service<FileNodeService>().createProjectFile(
                node.id!!,
                extension,
                project
            )
            node.file = virtualFile
        }

        project.service<ProjectTreeStateService>().insertInto(
            node,
            targetNode
        )

        return node
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }

}
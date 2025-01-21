package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.container.NoteNodeContainer
import dev.priporov.ideanotes.tree.factory.NoteNodeFactory
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto
import dev.priporov.ideanotes.tree.node.dto.NodeType
import dev.priporov.ideanotes.tree.service.ApplicationTreeStateService
import dev.priporov.ideanotes.tree.service.FileNodeService
import dev.priporov.ideanotes.util.WriteActionUtil

@Service(Service.Level.PROJECT)
class AppNoteTree : BaseTree<AppNoteTreeModel>() {

    override fun renameNode(value: String, node: NoteNode) {
        super.renameNode(value, node)
        service<NoteNodeFactory>().copy(node)
    }

    override fun delete(id: String) {
        val node = service<NoteNodeContainer>().getNodeById(id)
        if (node == null) {
            return
        }
        val applicationTreeStateService = service<ApplicationTreeStateService>()
        applicationTreeStateService.getChildrenRecursively(node.id!!).reversed().forEach { childId ->
            delete(childId)
        }

        val parent = node.parent as NoteNode

        applicationTreeStateService.delete(
            node.id!!, parent.id
        )

        node.removeFromParent()
        WriteActionUtil.runWriteAction {
            node.file?.delete(true)
        }

        service<NoteNodeContainer>().removeNode(node.id!!)

        getTreeModel().reload(parent)
    }

    override fun createInto(createNodeDto: CreateNodeDto, targetNode: NoteNode): NoteNode {
        val node = super.createInto(createNodeDto, targetNode)

        if (node.type != NodeType.PACKAGE && node.type != NodeType.UNKNOWN) {
            val extension = node.type.extension
            val virtualFile: VirtualFile = service<FileNodeService>().createApplicationFile(
                node.id!!,
                extension
            )
            node.file = virtualFile
        }

        service<ApplicationTreeStateService>().insertInto(
            node,
            targetNode
        )

        return node
    }

    override fun createNewInRoot(createNodeDto: CreateNodeDto): NoteNode {
        return createInto(createNodeDto, getRoot())
    }

}

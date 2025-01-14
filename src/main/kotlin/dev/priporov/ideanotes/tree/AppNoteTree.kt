package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.service.ApplicationTreeStateService
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.service.FileNodeService
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto

@Service(Service.Level.PROJECT)
class AppNoteTree : BaseTree<AppNoteTreeModel>() {

    override fun createNewInRoot(createNodeDto: CreateNodeDto): NoteNode {
        val node = super.createNewInRoot(createNodeDto)

        val virtualFile: VirtualFile = service<FileNodeService>().createApplicationFile(
            node.id!!,
            node.type!!.extension!!
        )
        node.file = virtualFile

        service<ApplicationTreeStateService>().insertInto(
            node,
            getTreeModel().root
        )

        return node
    }

}
package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.state.ApplicationTreeStateService
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto

@Service(Service.Level.PROJECT)
class AppNoteTree : BaseTree<AppNoteTreeModel>() {

    override fun createNewInRoot(createNodeDto: CreateNodeDto): NoteNode {
        val node = super.createNewInRoot(createNodeDto)

        service<ApplicationTreeStateService>().insertInto(
            node,
            getTreeModel().root
        )

        return node
    }

}
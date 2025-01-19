package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto
import dev.priporov.ideanotes.tree.service.ApplicationTreeStateService
import dev.priporov.ideanotes.tree.service.FileNodeService
import java.util.*

@Service(Service.Level.PROJECT)
class AppNoteTree : BaseTree<AppNoteTreeModel>() {

    override fun createInto(createNodeDto: CreateNodeDto, targetNode: NoteNode): NoteNode {
        val node = super.createInto(createNodeDto, targetNode)

        val virtualFile: VirtualFile = service<FileNodeService>().createApplicationFile(
            node.id!!,
            node.type!!.extension!!
        )
        node.file = virtualFile

        service<ApplicationTreeStateService>().insertInto(
            node,
            targetNode
        )

        return node
    }

//    override fun init(treeState: TreeStateDto) {
//        val fileNodeService = service<FileNodeService>()
//        val nodesGroupedById = treeState.nodesGroupedById.values.asSequence().map { stateNode ->
//            NoteNode().apply {
//                id = stateNode.id
//                name = stateNode.name
//                type = stateNode.type!!
//                file = fileNodeService.initVirtualFile(stateNode.id, stateNode.type!!.extension!!)
//            }
//        }.associateBy { it.id }
//
//        val rootChildren = treeState.hierarchy[getRoot().id]
//        if (rootChildren.isNullOrEmpty()) {
//            return
//        }
//
//        rootChildren.forEach { id ->
//            val node = nodesGroupedById[id]
//            getRoot().insert(node, getRoot().childCount)
//        }
//
//        val queue = LinkedList(rootChildren)
//
//        while (queue.isNotEmpty()) {
//            val nodeId = queue.poll()
//            val node = nodesGroupedById[nodeId]
//            if (node == null) {
//                continue
//            }
//            treeState.hierarchy[nodeId]?.forEach { childId ->
//                val childNode = nodesGroupedById[childId]
//                node.insert(childNode, node.childCount)
//
//                queue.add(childId)
//            }
//        }
//
//    }

    override fun createNewInRoot(createNodeDto: CreateNodeDto): NoteNode {
        return createInto(createNodeDto, getRoot())
    }

}
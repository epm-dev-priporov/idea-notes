package dev.priporov.ideanotes.init

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.factory.NoteNodeFactory
import dev.priporov.ideanotes.tree.model.ProjectNoteTreeModel
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.NodeType
import dev.priporov.ideanotes.tree.service.FileNodeService
import dev.priporov.ideanotes.tree.service.PluginStateService
import dev.priporov.ideanotes.tree.service.ProjectTreeStateService
import java.util.*

class ProjectInitializer : ProjectActivity {

    override suspend fun execute(project: Project) {
        initBaseDirIfNotExists(project)

        val model = project.service<ProjectNoteTreeModel>()
        initAppTreeModelFromState(
            project.service<ProjectTreeStateService>().getStateTree(),
            model.root,
            project
        )

        model.reload(model.root)
    }

    /** Init base directory of the plugin
     *
     */
    private fun initBaseDirIfNotExists(project: Project) {
        service<FileNodeService>().createBaseDirIfNotExists(
            service<PluginStateService>().getProjectBaseDir(project)
        )
    }

    private fun initAppTreeModelFromState(treeState: TreeStateDto, root: NoteNode, project: Project) {
        val fileNodeService = service<FileNodeService>()
        val noteNodeFactory = service<NoteNodeFactory>()

        val nodesGroupedById = treeState.nodesGroupedById.values.asSequence().map { stateNode ->
            noteNodeFactory.getNode(stateNode.name!!, stateNode.id!!).apply {
                type = stateNode.type!!
                if (stateNode.type != NodeType.PACKAGE) {
                    file = fileNodeService.initProjectVirtualFile(stateNode.id!!, stateNode.type!!.extension!!, project)
                }
            }
        }.associateBy { it.id }

        val rootChildren = treeState.hierarchy[root.id]
        if (rootChildren.isNullOrEmpty()) {
            return
        }

        rootChildren.forEach { id ->
            val node = nodesGroupedById[id]
            root.insert(node, root.childCount)
        }

        val queue = LinkedList(rootChildren)

        while (queue.isNotEmpty()) {
            val nodeId = queue.poll()
            val node = nodesGroupedById[nodeId]
            if (node == null) {
                continue
            }
            treeState.hierarchy[nodeId]?.forEach { childId ->
                val childNode = nodesGroupedById[childId]
                node.insert(childNode, node.childCount)

                queue.add(childId)
            }
        }
    }

}

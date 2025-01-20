package dev.priporov.ideanotes.init

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.ide.AppLifecycleListener
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import dev.priporov.ideanotes.state.TreeStateDto
import dev.priporov.ideanotes.tree.factory.NoteNodeFactory
import dev.priporov.ideanotes.tree.model.AppNoteTreeModel
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.NodeDefinitionDto
import dev.priporov.ideanotes.tree.service.ApplicationStateService
import dev.priporov.ideanotes.tree.service.ApplicationTreeStateService
import dev.priporov.ideanotes.tree.service.FileNodeService
import dev.priporov.ideanotes.tree.service.NodeDefinitionService
import java.util.*

class AppInitializer : AppLifecycleListener {
    private val mapper = ObjectMapper()

    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        loadNodeDefinitions()
        loadTreeStateFromFile()
        initBaseDirIfNotExists()
        initAppTreeModelFromState(
            service<ApplicationTreeStateService>().treeState,
            service<AppNoteTreeModel>().root
        )
    }


    fun method(){
//        ActionUtil.getShortcutSet("SelectInProjectView").shortcuts.also { shortcutSet ->
//            SelectFileInProjectViewAction()
//                .registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), textEditor.component)
//        }
    }

    /** load plugin global state from file

    */
    private fun loadTreeStateFromFile() {
        service<ApplicationTreeStateService>().init()
    }

    /** Init base directory of the plugin
     *
     */
    private fun initBaseDirIfNotExists() {
        service<FileNodeService>().createBaseDirIfNotExists(
            service<ApplicationStateService>().getApplicationBaseDIr()
        )
    }

    /** Load node definition from /resources/nodes/nodes.json
     *
     */
    private fun loadNodeDefinitions() {
        val inputStream = AppInitializer::class.java.getResourceAsStream("/nodes/nodes.json")
        val nodeDefinitions = mapper.readValue(inputStream, object : TypeReference<List<NodeDefinitionDto>>() {})
            .asSequence()
            .filter(this::filterByPlugin)
            .associateByTo(HashMap()) { it.type }

        service<NodeDefinitionService>().init(nodeDefinitions)
    }

    private fun initAppTreeModelFromState(treeState: TreeStateDto, root: NoteNode) {
        val fileNodeService = service<FileNodeService>()
        val nodesGroupedById = treeState.nodesGroupedById.values.asSequence().map { stateNode ->

            service<NoteNodeFactory>().getNode(stateNode.name!!, stateNode.id!!).apply {
                type = stateNode.type!!
                file = fileNodeService.initVirtualFile(stateNode.id!!, stateNode.type!!.extension!!)
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

    private fun filterByPlugin(definition: NodeDefinitionDto): Boolean {
        if (definition.plugin == null) {
            return true
        }
        val pluginId = PluginId.findId(definition.plugin)
        if (pluginId != null && PluginManager.getInstance().findEnabledPlugin(pluginId) != null) {
            return true
        }
        return false
    }

}
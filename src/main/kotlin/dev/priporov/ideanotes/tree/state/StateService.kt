package dev.priporov.ideanotes.tree.state

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.common.VirtualFileContainer
import dev.priporov.ideanotes.tree.exporting.STATE_FILE_NAME
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.FileNodeUtils
import java.io.File

@State(
    name = "StateService",
    storages = [Storage("ideanotes.xml")]
)
class StateService : PersistentStateComponent<ReaderState> {
    private val objectMapper = ObjectMapper()
    private val virtualFileContainer = service<VirtualFileContainer>()
    private var state = readFromJsonState()
    private var readerState = ReaderState()

    private fun readFromJsonState(): TreeState {
        val file = File("${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$STATE_FILE_NAME")
        if (!file.exists()) {
            return TreeState()
        }
        val bytes = file.readBytes()
        if (bytes.size != 0) {
            return objectMapper.readValue(bytes, TreeState::class.java)
        }
        return TreeState()
    }

    fun updateOrder(parent: FileTreeNode) = state.saveOrder(parent)

    fun saveNodeInfo(node: FileTreeNode) {
        state.saveNode(node)
        virtualFileContainer.addNode(node)
    }

    fun removeNodeInfo(node: FileTreeNode) {
        node.getFile()?.also { file -> virtualFileContainer.removeFile(file) }
        state.removeNodeInfo(node)
    }

    fun getTreeState(): TreeState {
        return state
    }

    override fun getState(): ReaderState {
        return readerState
    }

    override fun loadState(loadedState: ReaderState) {
        readerState = loadedState
    }

    fun contains(id: String, parentId: String): Boolean {
        return state.getOrder()[parentId]?.contains(id) ?: false
    }
}
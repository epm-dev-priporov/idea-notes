package dev.priporov.ideanotes.tree.importing

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.exporting.ExportService
import dev.priporov.ideanotes.tree.exporting.STATE_FILE_NAME
import dev.priporov.ideanotes.tree.node.ROOT_ID
import dev.priporov.ideanotes.tree.state.ReaderState
import dev.priporov.ideanotes.tree.state.TreeState
import dev.priporov.ideanotes.util.FileNodeUtils
import java.io.File

@Service
class ImportDeprecatedFormatService {

    private val objectMapper = ObjectMapper()

    fun importOldNotes(oldState: ReaderState){
        if ( oldState.isImported == true) {
            return
        }
        val file = File("${FileNodeUtils.baseDir}${FileNodeUtils.fileSeparator}$STATE_FILE_NAME")
        if (!file.exists()) {
            file.createNewFile()
        }
        val bytes = file.readBytes()
        var state = if (bytes.size != 0) {
            objectMapper.readValue(bytes, TreeState::class.java)
        } else {
            TreeState()
        }
        oldState.order[ROOT_ID].also {
            val elements = state.getOrderByParentId(ROOT_ID)
            if (elements != null) {
                it?.addAll(elements)
            }
        }
        state.addNodes(oldState.nodes)
        state.addOrder(oldState.order)
        service<ExportService>().saveStateToJsonFile(state)
        oldState.isImported = true
    }

}
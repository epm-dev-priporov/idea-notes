package dev.priporov.ideanotes.configuration

import com.intellij.openapi.components.service
import com.intellij.openapi.options.SearchableConfigurable
import dev.priporov.ideanotes.dialog.SettingsDialog
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.state.StateService
import javax.swing.JComponent

class NotesTreeConfigurable : SearchableConfigurable {
    private var state = false
    private val map = HashMap<NodeType, String>()
    private val gui: SettingsDialog by lazy {
        SettingsDialog(this)
    }

    override fun createComponent(): JComponent = gui.root

    override fun isModified() = state

    override fun apply() {
        val service = service<StateService>()
        map.forEach { (type, reader) -> service.state.setReader(type, reader) }
        state = false;
    }

    fun modified(reader: String, type: NodeType) {
        state = true
        map[type] = reader
    }

    override fun getDisplayName() = "Notes Tree"

    override fun getId() = "dev.priporov.ideanotes.configuration.NotesTreeConfigurable"
}
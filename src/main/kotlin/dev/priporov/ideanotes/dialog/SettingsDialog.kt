package dev.priporov.ideanotes.dialog

import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import dev.priporov.ideanotes.configuration.NotesTreeConfigurable
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.common.PluginDependency
import dev.priporov.ideanotes.tree.state.StateService
import dev.priporov.ideanotes.util.FileNodeUtils
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFormattedTextField
import javax.swing.JPanel

const val NATIVE = "Native"

class SettingsDialog(
    private val configurable: NotesTreeConfigurable,
    var root: JPanel = JPanel(),
    private var button: JButton = JButton(),
    private var formattedTextField: JFormattedTextField = JFormattedTextField(),
    private var csvComboBox: JComboBox<String> = JComboBox()
) {
    init {
        formattedTextField.apply {
            text = FileNodeUtils.baseDir.path
            isVisible = false
        }
        button.apply {
            isVisible = false
        }
        val dependenciesGroupedByType: Map<NodeType, PluginDependency> = ExtensionFileHelper
            .dependencyPlugins
            .associateBy { it.extensionData.type }

        csvComboBox.apply {
            initComboBox(dependenciesGroupedByType[NodeType.CSV], NodeType.CSV)
        }
    }

    private fun JComboBox<String>.initComboBox(pluginDependency: PluginDependency?, type: NodeType) {
        addItem(NATIVE)
        if (hasPlugin(pluginDependency)) {
            val id = pluginDependency!!.id
            addItem("Plugin: $id")
        }

        addItemListener(TestActionListener(configurable, type, this))
        val readerType = service<StateService>().state.getReaderType(type)
        if (readerType != null && PluginId.findId(readerType) != null) {
            setSelectedItem("Plugin: ${readerType}")
        } else {
            service<StateService>().state.setReader(type, NATIVE)
        }
    }

    private fun hasPlugin(pluginDependency: PluginDependency?) =
        pluginDependency != null && PluginId.findId(pluginDependency.id) != null

}

class TestActionListener(
    private val configurable: NotesTreeConfigurable,
    private val type: NodeType,
    private val jComboBox: JComboBox<String>
) : ItemListener {

    override fun itemStateChanged(e: ItemEvent?) {
        val selectedItem: String = jComboBox.selectedItem as String
        configurable.modified(toId(selectedItem), type)
    }

    private fun toId(selectedItem: String) = if (selectedItem.startsWith("Plugin: ")) {
        selectedItem.substringAfter("Plugin: ")
    } else {
        selectedItem
    }

}


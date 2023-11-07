package dev.priporov.ideanotes.dialog

import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
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

class SettingsDialog(
    var root: JPanel = JPanel(),
    var button: JButton = JButton(),
    var formattedTextField: JFormattedTextField = JFormattedTextField(),
    var csvComboBox: JComboBox<String> = JComboBox()
) {
    init {
        formattedTextField.apply {
            text = FileNodeUtils.baseDir.path
            isVisible = false
        }
        button.apply {
            isVisible = false
        }
        val dependenciesGroupedByType: Map<NodeType, PluginDependency> = ExtensionFileHelper.dependencyPlugins
            .associateBy { it.extensionData.type }

        csvComboBox.apply {
            val pluginDependency = dependenciesGroupedByType[NodeType.CSV]
            addItem("Native")

            if (hasPlugin(pluginDependency)) {
                val id = pluginDependency!!.id
                addItem("Plugin: $id")
            }
            addItemListener(TestActionListener(NodeType.CSV, this))
            val readerType = service<StateService>().state.getReaderType(NodeType.CSV)
            if (readerType != null && PluginId.findId(readerType) != null) {
                setSelectedItem("Plugin: ${readerType}")
            } else {
                service<StateService>().state.setReader(NodeType.CSV, "Native")
            }
        }
    }

    private fun hasPlugin(pluginDependency: PluginDependency?) =
        pluginDependency != null && PluginId.findId(pluginDependency.id) != null

}

class TestActionListener(
    private val type: NodeType,
    private val jComboBox: JComboBox<String>
) : ItemListener {

    override fun itemStateChanged(e: ItemEvent?) {
        val service = service<StateService>()
        val selectedItem: String = jComboBox.selectedItem as String
        val reader = toId(selectedItem)

        service.state.setReader(type, reader)
    }
}

private fun toId(selectedItem: String) = if (selectedItem.startsWith("Plugin: ")) {
    selectedItem.substringAfter("Plugin: ")
} else {
    selectedItem
}

package dev.priporov.ideanotes.tree.node.init

import com.fasterxml.jackson.annotation.JsonProperty
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.registry.Registry
import javax.swing.Icon


class NodeDefinition(
    @JsonProperty("index") var index: Int,
    @JsonProperty("type") var type: NodeType,
    @JsonProperty("extension") var extension: String,
    @JsonProperty("definition") var definition: String,
    @JsonProperty("icon") var iconPath: String,
    @JsonProperty("new_ui_icon") var newUiIconPath: String,
    @JsonProperty("plugin") var plugin: String? = null,
) {
    private var icon: Icon = IconLoader.getIcon(iconPath, javaClass)
    private var newUiIcon: Icon = IconLoader.getIcon(newUiIconPath, javaClass)

    fun getRequiredIcon() = if (isNewUi()) newUiIcon else icon

    companion object {
        fun isNewUi() = Registry.`is`("ide.experimental.ui")
    }
}

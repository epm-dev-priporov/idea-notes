package dev.priporov.ideanotes.tree.node.init

import com.fasterxml.jackson.annotation.JsonProperty
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.registry.Registry
import dev.priporov.ideanotes.icon.Icons
import javax.swing.Icon


class NodeDefinition(
    @JsonProperty("index") var index: Int,
    @JsonProperty("type") var type: NodeType,
    @JsonProperty("extension") var extension: String,
    @JsonProperty("definition") var definition: String,
    @JsonProperty("plugin") var plugin: String? = null,
) {
    private var icon: Icon? = type.icon
    private var newUiIcon: Icon? = type.newUiIcon

    fun getRequiredIcon() = if (isNewUi()) newUiIcon else icon


    companion object {
        fun isNewUi() = Registry.`is`("ide.experimental.ui")
    }
}

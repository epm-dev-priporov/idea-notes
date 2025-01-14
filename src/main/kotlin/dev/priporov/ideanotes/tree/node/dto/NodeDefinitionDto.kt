package dev.priporov.ideanotes.tree.node.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.intellij.openapi.util.registry.Registry
import dev.priporov.ideanotes.icon.Icons
import javax.swing.Icon


@JsonIgnoreProperties
class NodeDefinitionDto(
    @JsonProperty("index") var index: Int,
    @JsonProperty("type") var type: NodeType,
    @JsonProperty("extension") var extension: String,
    @JsonProperty("definition") var definition: String,
    @JsonProperty("plugin") var plugin: String? = null,
    @JsonProperty("ignore") var ignore: Boolean = false,
    @JsonProperty("ide") var listOfSupportedIde: List<String>? = null,
    @JsonProperty("os") var os: List<String>? = null,
) {
    private var icon: Icon? = type.icon
    private var newUiIcon: Icon? = type.newUiIcon

    fun getRequiredIcon() = if (Icons.isNewUi()) newUiIcon else icon

}

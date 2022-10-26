package dev.priporov.ideanotes.tree.common

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.extensions.PluginId
import dev.priporov.ideanotes.util.IconUtils
import javax.swing.Icon

class ExtensionFileHelper {
    companion object {
        val EXTENSIONS: MutableMap<String, ExtensionData> = sequenceOf(
            ExtensionData("txt", "Text node", "icons8-file-16.png", "icons-files-16.png"),
            ExtensionData("json", "Json node", "json/json16.png", "json/json16.png"),
            ExtensionData("xml", "Xml node", "yaml/yaml16.png", "yaml/yaml16.png"),
            ExtensionData("yaml", "Yaml node", "xml/xml16.png", "xml/xml16.png"),
            ExtensionData("sql", "Sql node", "sql/sql16.png", "sql/sql16.png"),
        ).associateByTo(HashMap()) { it.extension }

        init {
            val pluginId: PluginId? = PluginId.findId("com.jetbrains.restClient")
            if (pluginId != null && PluginManager.getInstance().findEnabledPlugin(pluginId) != null) {
                EXTENSIONS["http"] = ExtensionData("http", "Http node", "http/http16.png", "http/http16.png")
            }
        }

    }

}

class ExtensionData(
    val extension: String,
    val definition: String,
    leafIconPath: String,
    nodeIconPath: String,
    val leafIcon: Icon = IconUtils.toIcon(leafIconPath),
    val nodeIcon: Icon = IconUtils.toIcon(nodeIconPath),
)

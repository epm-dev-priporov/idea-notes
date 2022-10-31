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
            sequenceOf(
                    PluginDependency(
                            "org.intellij.plugins.markdown",
                            ExtensionData("md", "Markdown node", "md/markdown16.png", "md/markdown16.png"),
                    ),
                    PluginDependency(
                            "PlantUML integration",
                            ExtensionData("puml", "Puml node", "puml/puml16.png", "puml/puml16.png"),
                    ),
                    PluginDependency(
                            "com.jetbrains.restClient",
                            ExtensionData("http", "Http node", "http/http16.png", "http/http16.png")
                    ),
            ).forEach { applyExtension(it) }
        }

        private fun applyExtension(pluginDependency: PluginDependency) {
            val pluginId: PluginId? = PluginId.findId(pluginDependency.id)
            if (pluginId != null && PluginManager.getInstance().findEnabledPlugin(pluginId) != null) {
                val data = pluginDependency.extendionData
                EXTENSIONS[data.extension] = data
            }
        }

    }

}

class PluginDependency(
        val id: String,
        val extendionData: ExtensionData
)

class ExtensionData(
        val extension: String,
        val definition: String,
        leafIconPath: String,
        nodeIconPath: String,
        val leafIcon: Icon = IconUtils.toIcon(leafIconPath),
        val nodeIcon: Icon = IconUtils.toIcon(nodeIconPath),
)

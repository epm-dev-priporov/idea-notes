package dev.priporov.ideanotes.tree.common

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.extensions.PluginId
import dev.priporov.ideanotes.util.IconUtils
import javax.swing.Icon

const val DOCKERFILE = "Dockerfile"
const val DOCKER_COMPOSE = "Docker compose"
val UNKNOWN_FILE_ICON = IconUtils.toIcon("unknown.png")

class ExtensionFileHelper {
    companion object {

        val EXTENSIONS: MutableMap<String, ExtensionData> = sequenceOf(
            ExtensionData(0, "txt", "Text node", "icons8-file-16.png", "icons-files-16.png"),
            ExtensionData(1, "json", "Json node", "json/json16.png", "json/json16.png"),
            ExtensionData(2, "xml", "Xml node", "yaml/yaml16.png", "yaml/yaml16.png"),
            ExtensionData(3, "yaml", "Yaml node", "xml/xml16.png", "xml/xml16.png"),
            ExtensionData(4, "sql", "Sql node", "sql/sql16.png", "sql/sql16.png"),
            ExtensionData(13, "_", "Package", "package/package.png", "package/package.png"),
        ).associateByTo(HashMap()) { it.extension }

        val SORTED_EXTENSIONS: List<ExtensionData>

        init {
            val fullApplicationName = ApplicationInfo.getInstance().fullApplicationName
            when {
                isIntellijIdea(fullApplicationName) -> {
                    sequenceOf(
                        ExtensionData(10, "java", "Java node", "code/java.png", "code/java.png"),
                        ExtensionData(11, "kt", "Kotlin node", "code/kotlin.png", "code/kotlin.png"),
                        ExtensionData(12, "py", "Python node", "code/python.png", "code/python.png"),
                    ).forEach { EXTENSIONS[it.extension] = it }
                }
                isPyCharm(fullApplicationName) -> {
                    ExtensionData(12, "py", "Python node", "code/python.png", "code/python.png").also {
                        EXTENSIONS[it.extension] = it
                    }
                }
            }

            initPluginDependendFiles()

            SORTED_EXTENSIONS = EXTENSIONS.values.asSequence().sortedBy { it.index }.toList()
        }

        private fun isPyCharm(fullApplicationName: String) = fullApplicationName.startsWith("PyCharm")

        private fun isIntellijIdea(fullApplicationName: String) = fullApplicationName.startsWith("IntelliJ IDEA")

        private fun initPluginDependendFiles() {
            sequenceOf(
                PluginDependency(
                    "org.intellij.plugins.markdown",
                    ExtensionData(5, "md", "Markdown node", "md/markdown16.png", "md/markdown16.png"),
                ),
                PluginDependency(
                    "PlantUML integration",
                    ExtensionData(6, "puml", "Puml node", "puml/puml16.png", "puml/puml16.png"),
                ),
                PluginDependency(
                    "com.jetbrains.restClient",
                    ExtensionData(7, "http", "Http node", "http/http16.png", "http/http16.png")
                ),
                PluginDependency(
                    "Docker",
                    ExtensionData(8, "", DOCKERFILE, "docker/docker.png", "docker/docker.png"),
                ),
                PluginDependency(
                    "Docker",
                    ExtensionData(9, "yaml", DOCKER_COMPOSE, "docker/dockercompose.png", "docker/dockercompose.png"),
                ),
            ).forEach { applyExtension(it) }
        }

        private fun applyExtension(pluginDependency: PluginDependency) {
            val pluginId: PluginId? = PluginId.findId(pluginDependency.id)
            if (pluginId != null && PluginManager.getInstance().findEnabledPlugin(pluginId) != null) {
                val data = pluginDependency.extensionData
                EXTENSIONS[data.extension] = data
            }
        }

        fun containsExtension(extension: String) = EXTENSIONS.keys.contains(extension)

    }

}

class PluginDependency(
    val id: String,
    val extensionData: ExtensionData
)

class ExtensionData(
    val index: Int,
    val extension: String,
    val definition: String,
    leafIconPath: String,
    nodeIconPath: String,
    val leafIcon: Icon = IconUtils.toIcon(leafIconPath),
    val nodeIcon: Icon = IconUtils.toIcon(nodeIconPath),
)


package dev.priporov.ideanotes.tree.common

import com.intellij.icons.AllIcons
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.util.registry.Registry
import com.intellij.ui.IconManager
import dev.priporov.ideanotes.util.IconUtils
import java.util.*
import javax.swing.Icon

const val DOCKERFILE = "Dockerfile"
const val DOCKER_COMPOSE = "Docker compose"
val UNKNOWN_FILE_ICON = IconUtils.toIcon("unknown.png")

class ExtensionFileHelper {
    companion object {

        val EXTENSIONS: MutableMap<NodeType, ExtensionData> = sequenceOf(
            ExtensionData(0, NodeType.TXT, "txt", "Text node", "icons8-file-16.png", "icons-files-16.png"),
            ExtensionData(2, NodeType.JSON, "json", "Json node", "json/json16.png", "json/json16.png", newLeafIcon = AllIcons.FileTypes.Json),
            ExtensionData(3, NodeType.XML, "xml", "Xml node", "xml/xml16.png", "xml/xml16.png", newLeafIcon = AllIcons.FileTypes.Xml),
            ExtensionData(4, NodeType.YAML, "yaml", "Yaml node", "yaml/yaml16.png", "yaml/yaml16.png", newLeafIcon = AllIcons.FileTypes.Yaml),
            ExtensionData(5, NodeType.SQL, "sql", "Sql node", "sql/sql16.png", "sql/sql16.png"),
            ExtensionData(13, NodeType.PACKAGE, "packg", "Package", "package/package.png", "package/package.png", newLeafIcon = AllIcons.Nodes.Folder),
        ).associateByTo(HashMap()) { it.type }

        val SORTED_EXTENSIONS: List<ExtensionData>

        init {
            val fullApplicationName = ApplicationInfo.getInstance().fullApplicationName
            when {
                isIntellijIdea(fullApplicationName) || isAndroidStudio(fullApplicationName) -> {
                    sequenceOf(
                        ExtensionData(10, NodeType.JAVA, "java", "Java node", "code/java.png", "code/java.png", newLeafIcon = AllIcons.FileTypes.Java),
                        ExtensionData(11, NodeType.KOTLIN, "kt", "Kotlin node", "code/kotlin.png", "code/kotlin.png"),
                        ExtensionData(12, NodeType.PYTHON, "py", "Python node", "code/python.png", "code/python.png", newLeafIcon = IconUtils.toIcon("code/newPython.png")),
                    ).forEach { EXTENSIONS[it.type] = it }
                }

                isPyCharm(fullApplicationName) -> {
                    ExtensionData(12, NodeType.PYTHON, "py", "Python node", "code/python.png", "code/python.png", newLeafIcon = IconUtils.toIcon("code/newPython.png")).also {
                        EXTENSIONS[it.type] = it
                    }
                }
            }

            initPluginDependendFiles()

            SORTED_EXTENSIONS = EXTENSIONS.values.asSequence().sortedBy { it.index }.toList()
        }

        private fun isPyCharm(fullApplicationName: String) = fullApplicationName.startsWith("PyCharm")

        private fun isIntellijIdea(fullApplicationName: String) = fullApplicationName.startsWith("IntelliJ IDEA")
        private fun isAndroidStudio(fullApplicationName: String) = fullApplicationName.startsWith("Android")

        private fun initPluginDependendFiles() {
            sequenceOf(
                PluginDependency(
                    "org.intellij.plugins.markdown",
                    ExtensionData(
                        1,
                        NodeType.MARK_DOWN,
                        "md",
                        "Markdown node",
                        "md/markdown16.png",
                        "md/markdown16.png"
                    ),
                ),
                PluginDependency(
                    "PlantUML integration",
                    ExtensionData(6, NodeType.PUML, "puml", "Puml node", "puml/puml16.png", "puml/puml16.png"),
                ),
                PluginDependency(
                    "com.jetbrains.restClient",
                    ExtensionData(7, NodeType.HTTP, "http", "Http node", "http/http16.png", "http/http16.png")
                ),
                PluginDependency(
                    "Docker",
                    ExtensionData(8, NodeType.DOCKERFILE, "", DOCKERFILE, "docker/docker.png", "docker/docker.png", newLeafIcon = IconUtils.toIcon("docker/newDocker.png")),
                ),
                PluginDependency(
                    "Docker",
                    ExtensionData(
                        9,
                        NodeType.DOCKER_COMPOSE,
                        "yaml",
                        DOCKER_COMPOSE,
                        "docker/dockercompose.png",
                        "docker/dockercompose.png"
                    ),
                ),
            ).forEach { applyExtension(it) }
        }

        private fun applyExtension(pluginDependency: PluginDependency) {
            val pluginId: PluginId? = PluginId.findId(pluginDependency.id)
            if (pluginId != null && PluginManager.getInstance().findEnabledPlugin(pluginId) != null) {
                val data = pluginDependency.extensionData
                EXTENSIONS[data.type] = data
            }
        }

        fun containsExtension(extension: String) = SORTED_EXTENSIONS.find { it.extension == extension } != null

    }

}

class PluginDependency(
    val id: String,
    val extensionData: ExtensionData
)

class ExtensionData(
    val index: Int,
    val type: NodeType,
    val extension: String,
    val definition: String,
    leafIconPath: String,
    nodeIconPath: String,
    val leafIcon: Icon = IconUtils.toIcon(leafIconPath),
    val newLeafIcon: Icon = leafIcon,
    val nodeIcon: Icon = IconUtils.toIcon(nodeIconPath),
    val newNodeIcon: Icon = newLeafIcon
) {
    fun getRequiredLeafIcon() = if (Registry.`is`("ide.experimental.ui")) newLeafIcon else leafIcon
    fun getRequiredNodeIcon() = if (Registry.`is`("ide.experimental.ui")) newNodeIcon else nodeIcon
}

enum class NodeType {
    TXT,
    YAML,
    XML,
    JSON,
    PUML,
    DOCKER_COMPOSE,
    DOCKERFILE,
    HTTP,
    SQL,
    PACKAGE,
    PYTHON,
    JAVA,
    KOTLIN,
    MARK_DOWN
}

 private fun loadIcon(path: String, cacheKey: Int = Random().nextInt(), flags: Int = 2) =
    IconManager.getInstance().loadRasterizedIcon(
        path,
        AllIcons::class.java.classLoader,
        cacheKey,
        flags
    );

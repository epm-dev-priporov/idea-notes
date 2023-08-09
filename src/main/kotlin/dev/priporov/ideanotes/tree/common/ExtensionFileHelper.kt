package dev.priporov.ideanotes.tree.common

import com.intellij.icons.AllIcons
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.registry.Registry
import dev.priporov.ideanotes.util.IconUtils
import java.util.*
import javax.swing.Icon

const val DOCKERFILE = "Dockerfile"
const val DOCKER_COMPOSE = "Docker compose"

object Icons {
    val UNKNOWN_FILE_ICON = IconLoader.getIcon("/icons/unknown.png", javaClass)
    val NEW_ILE_ICON = IconLoader.getIcon("/icons/newUnknown.png", javaClass)
}

class ExtensionFileHelper {
    companion object {

        val EXTENSIONS: MutableMap<NodeType, ExtensionData> = sequenceOf(
            ExtensionData(0, NodeType.TXT, "txt", "Text node", "icons8-file-16.png", "icons-files-16.png"),
            ExtensionData(
                2,
                NodeType.JSON,
                "json",
                "Json node",
                "json/json16.png",
                newLeafIcon = AllIcons.FileTypes.Json
            ),
            ExtensionData(3, NodeType.XML, "xml", "Xml node", "xml/xml16.png", newLeafIcon = AllIcons.FileTypes.Xml),
            ExtensionData(
                4,
                NodeType.YAML,
                "yaml",
                "Yaml node",
                "yaml/yaml16.png",
                newLeafIcon = AllIcons.FileTypes.Yaml
            ),
            ExtensionData(
                5,
                NodeType.SQL,
                "sql",
                "Sql node",
                "sql/sql16.png",
                newLeafIcon = IconLoader.getIcon("/icons/sql/newSql.png", javaClass)
            ),
            ExtensionData(
                13,
                NodeType.PACKAGE,
                "packg",
                "Package",
                "package/package.png",
                newLeafIcon = AllIcons.Nodes.Folder
            ),
            ExtensionData(
                0,
                NodeType.IMAGE_PNG,
                "png",
                "PNG image",
                "image/img_old.png",
                newLeafIcon = IconLoader.getIcon("/icons/image/img.png", javaClass),
                ignore = true
            ),
            ExtensionData(
                0,
                NodeType.IMAGE_JPG,
                "jpg",
                "JPG image",
                "image/img_old.png",
                newLeafIcon = IconLoader.getIcon("/icons/image/img.png", javaClass),
                ignore = true
            ),
            ExtensionData(
                0,
                NodeType.SVG_JPG,
                "svg",
                "SVG",
                "image/img_old.png",
                newLeafIcon = IconLoader.getIcon("/icons/image/img.png", javaClass),
                ignore = true
            ),
            ExtensionData(
                0,
                NodeType.PDF,
                "pdf",
                "pdf document",
                "pdf/pdf.png",
                ignore = true
            ),
            ExtensionData(
                0,
                NodeType.YML,
                "yml",
                "yml document",
                "yaml/yaml16.png",
                newLeafIcon = AllIcons.FileTypes.Yaml,
                ignore = true
            ),
            ExtensionData(
                0,
                NodeType.DOC,
                "doc",
                "doc document",
                "doc/doc.png",
                ignore = true
            ),
            ExtensionData(
                0,
                NodeType.DOCX,
                "docx",
                "docx document",
                "doc/doc.png",
                ignore = true
            ),
            ExtensionData(
                0,
                NodeType.EXCEL,
                "elsx",
                "Excel table",
                "excel/excel.png",
                ignore = true
            ),
            ExtensionData(
                14,
                NodeType.SH,
                "sh",
                "Sh script",
                "sh/sh.png",
                newLeafIcon = IconLoader.getIcon("/icons/sh/shNew.png", javaClass),
            ),
        ).associateByTo(HashMap()) { it.type }

        val SORTED_EXTENSIONS: List<ExtensionData>

        init {
            val fullApplicationName = ApplicationInfo.getInstance().fullApplicationName
            when {
                isIntellijIdea(fullApplicationName) || isAndroidStudio(fullApplicationName) -> {
                    sequenceOf(
                        ExtensionData(
                            10,
                            NodeType.JAVA,
                            "java",
                            "Java node",
                            "code/java.png",
                            newLeafIcon = AllIcons.FileTypes.Java
                        ),
                        ExtensionData(
                            11,
                            NodeType.KOTLIN,
                            "kt",
                            "Kotlin node",
                            "code/kotlin.png",
                            newLeafIcon = IconUtils.toIcon("code/newKotlin.png")
                        ),
                        ExtensionData(
                            12,
                            NodeType.PYTHON,
                            "py",
                            "Python node",
                            "code/python.png",
                            newLeafIcon = IconUtils.toIcon("code/newPython.png")
                        ),
                    ).forEach { EXTENSIONS[it.type] = it }
                }

                isPyCharm(fullApplicationName) -> {
                    ExtensionData(
                        12,
                        NodeType.PYTHON,
                        "py",
                        "Python node",
                        "code/python.png",
                        newLeafIcon = IconUtils.toIcon("code/newPython.png")
                    ).also {
                        EXTENSIONS[it.type] = it
                    }
                }
            }

            initPluginDependedFiles()

            if (isMacOrLinux()) {
                ExtensionData(
                    16,
                    NodeType.SOFT_LINK,
                    "",
                    "Symbolic link",
                    "link/sybolicLink.png"
                ).also {
                    EXTENSIONS[it.type] = it
                }
            }

            SORTED_EXTENSIONS = EXTENSIONS.values.asSequence().sortedBy { it.index }.filter { !it.ignore }.toList()
        }

        private fun isMacOrLinux(): Boolean {
            val osType = System.getProperty("os.name").lowercase(Locale.ENGLISH)
            return osType.contains("mac") or (osType == "linux")
        }

        private fun isPyCharm(fullApplicationName: String) = fullApplicationName.startsWith("PyCharm")

        private fun isIntellijIdea(fullApplicationName: String) = fullApplicationName.startsWith("IntelliJ IDEA")
        private fun isAndroidStudio(fullApplicationName: String) = fullApplicationName.startsWith("Android")

        private fun initPluginDependedFiles() {
            sequenceOf(
                PluginDependency(
                    "org.intellij.plugins.markdown",
                    ExtensionData(
                        1,
                        NodeType.MARK_DOWN,
                        "md",
                        "Markdown node",
                        "md/markdown16.png",
                        newLeafIcon = IconLoader.getIcon("/icons/md/newMd.png", javaClass)
                    ),
                ),
                PluginDependency(
                    "PlantUML integration",
                    ExtensionData(6, NodeType.PUML, "puml", "Puml node", "puml/puml16.png"),
                ),
                PluginDependency(
                    "com.jetbrains.restClient",
                    ExtensionData(7, NodeType.HTTP, "http", "Http node", "http/http16.png")
                ),
                PluginDependency(
                    "Docker",
                    ExtensionData(
                        8,
                        NodeType.DOCKERFILE,
                        "",
                        DOCKERFILE,
                        "docker/docker.png",
                        newLeafIcon = IconUtils.toIcon("docker/newDocker.png")
                    ),
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
                PluginDependency(
                    "net.seesharpsoft.intellij.plugins.csv",
                    ExtensionData(
                        15,
                        NodeType.CSV,
                        "csv",
                        "Csv table",
                        "csv/csv.png",
                        newLeafIcon = IconLoader.getIcon("/icons/csv/csvNew.png", javaClass),
                    ),
                ),
                PluginDependency(
                    "dev.meanmail.plugin.nginx-intellij-plugin",
                    ExtensionData(
                        16,
                        NodeType.CONF,
                        "conf",
                        "nginx config",
                        "nginx/nginx.png",
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

        fun containsExtension(extension: String) = EXTENSIONS.values.find { it.extension == extension } != null

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
    nodeIconPath: String = leafIconPath,
    private val leafIcon: Icon = IconUtils.toIcon(leafIconPath),
    val newLeafIcon: Icon = leafIcon,
    private val nodeIcon: Icon = IconUtils.toIcon(nodeIconPath),
    private val newNodeIcon: Icon = newLeafIcon,
    val ignore: Boolean = false,
) {
    fun getRequiredLeafIcon() = if (isNewUi()) newLeafIcon else leafIcon
    fun getRequiredNodeIcon() = if (isNewUi()) newNodeIcon else nodeIcon

    companion object {
        fun isNewUi() = Registry.`is`("ide.experimental.ui")
    }
}

enum class NodeType(val extension: String?) {
    TXT("txt"),
    YAML("yaml"),
    XML("xml"),
    JSON("json"),
    PUML("puml"),
    DOCKER_COMPOSE("yaml"),
    DOCKERFILE(null),
    HTTP("http"),
    SQL("sql"),
    PACKAGE(null),
    PYTHON("py"),
    JAVA("java"),
    KOTLIN("kr"),
    MARK_DOWN("md"),
    IMAGE_PNG("png"),
    IMAGE_JPG("jpg"),
    SVG_JPG("svg"),
    PDF("pdf"),
    SOFT_LINK(""),
    SH("sh"),
    CSV("csv"),
    YML("yml"),
    DOC("icons/doc"),
    DOCX("docx"),
    CONF("conf"),
    EXCEL("xlsx"),
    UNKNOWN(null);

    companion object {
        private val map = values().associateBy { it.extension }
        fun fromExtension(value: String) = map.getOrDefault(value, UNKNOWN)
    }

}


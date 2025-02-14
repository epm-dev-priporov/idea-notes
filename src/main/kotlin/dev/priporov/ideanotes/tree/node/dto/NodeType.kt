package dev.priporov.ideanotes.tree.node.dto

import dev.priporov.ideanotes.icon.Icons
import javax.swing.Icon

enum class NodeType(
    val extension: String?,
    val icon: Icon?,
    val newUiIcon: Icon?
) {
    TXT("txt", Icons.Extension.TXT, Icons.Extension.NEW_UI_TXT),
    YAML("yaml", Icons.Extension.YAML, Icons.Extension.NEW_UI_YAML),
    XML("xml", Icons.Extension.XML, Icons.Extension.NEW_UI_XML),
    JSON("json", Icons.Extension.JSON, Icons.Extension.NEW_UI_JSON),
    PUML("puml", Icons.Extension.PUML, Icons.Extension.NEW_UI_PUML),
    DOCKER_COMPOSE("yaml", Icons.Extension.DOCKER_COMPOSE, Icons.Extension.NEW_UI_DOCKER_COMPOSE),
    DOCKERFILE(null, Icons.Extension.DOCKERFILE, Icons.Extension.NEW_UI_DOCKERFILE),
    HTTP("http", Icons.Extension.HTTP, Icons.Extension.NEW_UI_HTTP),
    SQL("sql", Icons.Extension.SQL, Icons.Extension.NEW_UI_SQL),
    PACKAGE(null, Icons.Extension.PACKAGE, Icons.Extension.NEW_UI_PACKAGE),
    PYTHON("py", Icons.Extension.PYTHON, Icons.Extension.NEW_UI_PYTHON),
    CPP("cpp", Icons.Extension.CPP, Icons.Extension.NEW_UI_CPP),
    C("c", Icons.Extension.C, Icons.Extension.NEW_UI_C),
    GO("go", Icons.Extension.GO, Icons.Extension.NEW_UI_GO),
    RUBY("rb", Icons.Extension.RUBY, Icons.Extension.NEW_UI_RUBY),
    JAVA("java", Icons.Extension.JAVA, Icons.Extension.NEW_UI_JAVA),
    GROOVY("groovy", Icons.Extension.GROOVY, Icons.Extension.NEW_UI_GROOVY),
    KOTLIN("kt", Icons.Extension.KOTLIN, Icons.Extension.NEW_UI_KOTLIN),
    MARK_DOWN("md", Icons.Extension.MARKDOWN, Icons.Extension.NEW_UI_MARKDOWN),
    IMAGE_PNG("png", Icons.Extension.IMAGE, Icons.Extension.NEW_UI_IMAGE),
    IMAGE_JPG("jpg", Icons.Extension.IMAGE, Icons.Extension.NEW_UI_IMAGE),
    IMAGE_JPEG("jpeg", Icons.Extension.IMAGE, Icons.Extension.NEW_UI_IMAGE),
    SVG_JPG("svg", Icons.Extension.IMAGE, Icons.Extension.NEW_UI_IMAGE),
    PDF("pdf", Icons.Extension.PDF, Icons.Extension.NEW_UI_PDF),
//    SOFT_LINK("", Icons.Extension.SOFT_LINK, Icons.Extension.NEW_UI_SOFT_LINK),
    SH("sh", Icons.Extension.SH, Icons.Extension.NEW_UI_SH),
    CSV("csv", Icons.Extension.CSV, Icons.Extension.NEW_UI_CSV),
    YML("yml", Icons.Extension.YAML, Icons.Extension.NEW_UI_YAML),
    DOC("icons/doc", Icons.Extension.DOC, Icons.Extension.NEW_UI_DOC),
    DOCX("docx", Icons.Extension.DOC, Icons.Extension.NEW_UI_DOC),
    CONF("conf", Icons.Extension.NGINX_CONF, Icons.Extension.NEW_UI_NGINX_CONF),
    EXCEL("xlsx", Icons.Extension.EXCEL, Icons.Extension.NEW_UI_EXCEL),
    LOG("log", Icons.Extension.LOG, Icons.Extension.NEW_UI_LOG),
    UNKNOWN(null, Icons.Extension.UNKNOWN, Icons.Extension.NEW_UI_UNKNOWN),;

    companion object {
        private val map = values().associateBy { it.extension }
        fun fromExtension(value: String) = map.getOrDefault(value, UNKNOWN)
    }

    fun getRequiredIcon() = if (Icons.isNewUi()) newUiIcon else icon
}


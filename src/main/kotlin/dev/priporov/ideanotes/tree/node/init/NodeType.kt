package dev.priporov.ideanotes.tree.node.init

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
    CPP("cpp"),
    C("c"),
    GO("go"),
    RUBY("rb"),
    JAVA("java"),
    GROOVY("groovy"),
    KOTLIN("kt"),
    MARK_DOWN("md"),
    IMAGE_PNG("png"),
    IMAGE_JPG("jpg"),
    IMAGE_JPEG("jpeg"),
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
    LOG("log"),
    UNKNOWN(null);

    companion object {
        private val map = values().associateBy { it.extension }
        fun fromExtension(value: String) = map.getOrDefault(value, UNKNOWN)
    }

}


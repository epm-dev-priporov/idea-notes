package dev.priporov.ideanotes.icon

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.registry.Registry

object Icons {
    object Extension {
        val JSON = from("/icons/json/json16.png")
        val NEW_UI_JSON = AllIcons.FileTypes.Json
        val XML = from("/icons/xml/xml16.png")
        val NEW_UI_XML = AllIcons.FileTypes.Xml
        val YAML = from("/icons/yaml/yaml16.png")
        val NEW_UI_YAML = AllIcons.FileTypes.Yaml
        val SQL = from("/icons/sql/sql16.png")
        val NEW_UI_SQL = from("/icons/sql/newSql.png")
        val PACKAGE = from("/icons/package/package.png")
        val NEW_UI_PACKAGE = AllIcons.Nodes.Folder
        val MARKDOWN = from("/icons/md/markdown16.png")
        val NEW_UI_MARKDOWN = from("/icons/md/markdown16.png")
        val PUML = from("/icons/puml/puml16.png")
        val NEW_UI_PUML = from("/icons/puml/puml16.png")
        val HTTP = from("/icons/http/http16.png")
        val NEW_UI_HTTP = from("/icons/http/http16.png")
        val DOCKERFILE = from("/icons/docker/docker.png")
        val NEW_UI_DOCKERFILE = from("/icons/docker/newDocker.png")
        val DOCKER_COMPOSE = from("/icons/docker/dockercompose.png")
        val NEW_UI_DOCKER_COMPOSE = from("/icons/docker/dockercompose.png")
        val CSV = from("/icons/csv/csv.png")
        val NEW_UI_CSV = from("/icons/csv/csvNew.png")
        val NGINX_CONF = from("/icons/nginx/nginx.png")
        val NEW_UI_NGINX_CONF = from("/icons/nginx/nginx.png")
        val IMAGE = from("/icons/image/img_old.png")
        val NEW_UI_IMAGE = from("/icons/image/img.png")
        val PDF = from("/icons/pdf/pdf.png")
        val NEW_UI_PDF = from("/icons/pdf/pdf.png")
        val DOC = from("/icons/doc/doc.png")
        val NEW_UI_DOC = from("/icons/doc/doc.png")
        val EXCEL = from("/icons/excel/excel.png")
        val NEW_UI_EXCEL = from("/icons/excel/excel.png")
        val LOG = from("/icons/icons8-file-16.png")
        val NEW_UI_LOG = from("/icons/icons8-file-16.png")
        val SH = from("/icons/sh/sh.png")
        val NEW_UI_SH = from("/icons/sh/shNew.png")
        val JAVA = from("/icons/code/java.png")
        val NEW_UI_JAVA = AllIcons.FileTypes.Java
        val KOTLIN = from("/icons/code/kotlin.png")
        val NEW_UI_KOTLIN = from("/icons/code/newKotlin.png")
        val GROOVY = from("/icons/code/groovy.png")
        val NEW_UI_GROOVY = from("/icons/code/newGroovy.png")
        val PYTHON = from("/icons/code/python.png")
        val NEW_UI_PYTHON = from("/icons/code/newPython.png")
        val GO = from("/icons/code/go.png")
        val NEW_UI_GO = from("/icons/code/go.png")
        val C = from("/icons/code/c.png")
        val NEW_UI_C = from("/icons/code/c.png")
        val CPP = from("/icons/code/cpp.png")
        val NEW_UI_CPP = from("/icons/code/cpp.png")
        val RUBY = from("/icons/code/ruby.png")
        val NEW_UI_RUBY = from("/icons/code/ruby.png")
        val SOFT_LINK = from("/icons/link/sybolicLink.png")
        val NEW_UI_SOFT_LINK = from("/icons/link/sybolicLink.png")
        val TXT = from("/icons/icons8-file-16.png")
        val NEW_UI_TXT = from("/icons/icons8-file-16.png")
        val UNKNOWN = from("/icons/unknown.png")
        val NEW_UI_UNKNOWN = from("/icons/newUnknown.png")
    }

    object ToolbarFactory {
        val EXPORT_ICON = from("/icons/menu/export.png")
        val SETTINGS_ICON = from("/icons/menu/settings.png")
        val IMPORT_ICON = from("/icons/menu/import.png")
        val COLLAPSE_ICON = from("/icons/collapse_expand/collapseComponent.png")
        val EXPAND_ICON = from("/icons/collapse_expand/expandComponent.png")
    }

    object PopUpMenu {
        val ADD_NODE_ICON = from("/icons/menu/addIcon.png")
        val PASTE_ICON = from("/icons/menu/paste.png")
        val CUT_ICON = from("/icons/menu/cut.png")
        val COPY_ICON = from("/icons/menu/copy.png")
        val DELETE_ICON =  from("/icons/menu/deleteIcon.png")
    }

    fun from(path: String) = IconLoader.getIcon(path, javaClass)
    fun isNewUi() = Registry.`is`("ide.experimental.ui")
}
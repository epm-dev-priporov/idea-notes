package dev.priporov.ideanotes.icon

import com.intellij.openapi.util.IconLoader

object Icons {
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
}
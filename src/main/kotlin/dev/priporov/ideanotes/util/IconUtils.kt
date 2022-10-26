package dev.priporov.ideanotes.util

import dev.priporov.ideanotes.tree.common.NoteCellRenderer
import javax.swing.ImageIcon

object IconUtils {

    fun toIcon(path: String): ImageIcon {
        return ImageIcon(NoteCellRenderer::class.java.getResource("/icons/${path}"))
    }

}
package dev.priporov.ideanotes.tree.common

import com.intellij.ide.util.treeView.NodeRenderer
import dev.priporov.ideanotes.tree.node.FileTreeNode
import javax.swing.ImageIcon
import javax.swing.JTree

class NoteCellRenderer : NodeRenderer() {

    override fun customizeCellRenderer(
        tree: JTree,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {

        if (value is FileTreeNode) {
            if (leaf) {
                when (value.extension) {
                    "txt" -> setIcon(toIcon(toIconPath("icons8-file-16.png")))
                    "json" -> setIcon(toIcon(toIconPath("json/json16.png")))
                    "yaml" -> setIcon(toIcon(toIconPath("yaml/yaml16.png")))
                    "xml" -> setIcon(toIcon(toIconPath("xml/xml16.png")))
                    "sql" -> setIcon(toIcon(toIconPath("sql/sql16.png")))
                }
            } else {
                when (value.extension) {
                    "txt" -> setIcon(toIcon(toIconPath("icons-files-16.png")))
                    "json" -> setIcon(toIcon(toIconPath("json/json16.png")))
                    "yaml" -> setIcon(toIcon(toIconPath("yaml/yaml16.png")))
                    "xml" -> setIcon(toIcon(toIconPath("xml/xml16.png")))
                    "sql" -> setIcon(toIcon(toIconPath("sql/sql16.png")))
                }
            }
        }

        super.customizeCellRenderer(tree, value, selected, expanded, leaf, row, hasFocus)
    }

    private fun toIconPath(name: String) = "/icons/${name}"

    private fun toIcon(path: String) = ImageIcon(NoteCellRenderer::class.java.getResource(path))
}

package dev.priporov.ideanotes.tree.common

import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.util.treeView.NodeRenderer
import com.intellij.openapi.extensions.PluginId
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
            val path = if (leaf) {
                ExtensionFileHelper.EXTENSIONS[value.extension]?.leafIconPath ?: "icons8-file-16.png"
            } else {
                ExtensionFileHelper.EXTENSIONS[value.extension]?.nodeIconPath ?: "icons-files-16.png"
            }
            setIcon(toIcon(toIconPath(path)))
        }

        super.customizeCellRenderer(tree, value, selected, expanded, leaf, row, hasFocus)
    }

    private fun toIconPath(name: String) = "/icons/${name}"

    private fun toIcon(path: String) = ImageIcon(NoteCellRenderer::class.java.getResource(path))
}

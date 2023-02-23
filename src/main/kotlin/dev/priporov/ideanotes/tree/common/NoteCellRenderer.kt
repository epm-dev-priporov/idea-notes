package dev.priporov.ideanotes.tree.common

import com.intellij.ide.util.treeView.NodeRenderer
import dev.priporov.ideanotes.tree.node.FileTreeNode
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
            val extensionData = ExtensionFileHelper.EXTENSIONS[value.type]
            val icon = if (leaf) extensionData?.getRequiredLeafIcon() else extensionData?.getRequiredNodeIcon()
            if (icon == null) {
                setIcon(UNKNOWN_FILE_ICON)
            } else {
                setIcon(icon)
            }
        }
        super.customizeCellRenderer(tree, value, selected, expanded, leaf, row, hasFocus)
    }

}

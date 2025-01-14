package dev.priporov.ideanotes.tree.model

import com.intellij.ide.util.treeView.NodeRenderer
import com.intellij.openapi.components.Service
import dev.priporov.ideanotes.tree.node.NoteNode
import javax.swing.JTree

@Service
class NoteTreeCellRenderer : NodeRenderer() {

    override fun customizeCellRenderer(
        tree: JTree,
        node: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {
        if (node is NoteNode) {
            setIcon(node.type.getRequiredIcon())
        }
        super.customizeCellRenderer(tree, node, selected, expanded, leaf, row, hasFocus)
    }

}
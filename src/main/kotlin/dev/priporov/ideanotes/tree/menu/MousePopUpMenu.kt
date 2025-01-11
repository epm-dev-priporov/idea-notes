package dev.priporov.ideanotes.tree.menu

import com.intellij.openapi.actionSystem.DefaultActionGroup
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.action.*
import dev.priporov.ideanotes.tree.node.NoteNode

class MousePopUpMenu(tree: BaseTree<*>, selectedNodeClicked: Boolean) : DefaultActionGroup() {
    init {
        val targetNode = tree.selectionPath?.lastPathComponent as? NoteNode

        if (targetNode == null || !selectedNodeClicked) {
            add(NewNodeActionGroup(tree, "New node"))
            add(PasteNodeAction(tree, "Past"))
        } else {
            add(NewNodeActionGroup(tree, "New node"))
            add(OpenInEditorAction(tree, "Open"))
            add(AddNodeActionGroup(tree, targetNode, "Add child node"))
            add(CutNodeAction(tree, "Cut"))
            add(CopyNodeAction(tree, "Copy"))
            add(PasteNodeAction(tree, "Past"))
            add(RenameNodeAction(tree, "Rename"))
            add(ExportNodeAction(tree, "Export"))
            add(DeleteNodeAction(tree, targetNode, "Delete"))
        }
    }
}

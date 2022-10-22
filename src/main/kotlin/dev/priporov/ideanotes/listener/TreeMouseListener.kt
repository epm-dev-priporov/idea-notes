package dev.priporov.ideanotes.listener

import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.TreePopUpMenuManager
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.SwingUtilities

class TreeMouseListener(private val tree: NoteTree) : MouseListener {

    override fun mouseClicked(e: MouseEvent) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            val component = tree.getSelectedNode() ?: return
            if (isDoubledClickedByNode(e) && isSelectedNodeClicked(e)) {
                tree.openInEditor(component)
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            TreePopUpMenuManager.createPopUpMenu(tree, e.locationOnScreen, isSelectedNodeClicked(e))
        }

    }

    private fun isDoubledClickedByNode(e: MouseEvent): Boolean = e.clickCount == 2

    private fun isSelectedNodeClicked(e: MouseEvent) = tree.getPathForLocation(e.x, e.y) == tree.selectionPath

    override fun mousePressed(e: MouseEvent?) {}

    override fun mouseReleased(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}

}

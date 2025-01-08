package dev.priporov.ideanotes.tree.listner

import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.menu.MousePopUpMenu
import dev.priporov.ideanotes.tree.menu.TreePopUpMenuManager
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.SwingUtilities

class TreeMouseListener(private val tree: BaseTree<*>) : MouseListener {

    override fun mouseClicked(e: MouseEvent) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            val node = tree.getSelectedNode() ?: return
            if (isDoubledClickedByNode(e) && isSelectedNodeClicked(e)) {
                tree.openInEditor(node)
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            TreePopUpMenuManager.createPopUpMenu(
                tree,
                e.locationOnScreen,
                MousePopUpMenu(tree, isSelectedNodeClicked(e))
            )
        }

    }

    private fun isDoubledClickedByNode(e: MouseEvent): Boolean = e.clickCount == 2

    private fun isSelectedNodeClicked(e: MouseEvent) = TreeUtil.getPathForLocation(tree, e.x, e.y) == tree.selectionPath

    override fun mousePressed(e: MouseEvent?) {}

    override fun mouseReleased(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}

}

package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable


class CopyNodeAction(
    private val tree: BaseTree<*>,
    value: String? = null
) : AnAction(value, "", Icons.PopUpMenu.COPY_ICON) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.lastSelectedPathComponent as? FileTreeNode ?: return

        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val clipboardOwner = ClipboardOwner { _, _ -> }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

}

class NodeCopyTransferable() : Transferable, ClipboardOwner {
    override fun getTransferDataFlavors(): Array<DataFlavor> {
        TODO("Not yet implemented")
    }

    override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTransferData(flavor: DataFlavor?): Any {
        TODO("Not yet implemented")
    }

    override fun lostOwnership(clipboard: Clipboard?, contents: Transferable?) {
        TODO("Not yet implemented")
    }


}
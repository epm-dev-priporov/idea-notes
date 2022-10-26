package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.dto.NodeCopyData
import dev.priporov.ideanotes.dto.NodeCutData
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.state.NodeInfo
import dev.priporov.ideanotes.util.FileNodeUtils.Companion.readFileContentByteArray
import java.awt.Toolkit
import java.awt.datatransfer.*
import java.awt.datatransfer.DataFlavor.stringFlavor

class CutNodeAction(
    private val tree: NoteTree,
    value: String? = null
) : AnAction(value) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.lastSelectedPathComponent as? FileTreeNode ?: return

        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val clipboardOwner = ClipboardOwner { _, _ -> }

        val cutData = NodeCutData(node, tree)

        val transferable = NodeCutTransferable(cutData)

        clipboard.setContents(transferable, clipboardOwner)
        tree.delete(node)
    }


}

class NodeCutTransferable(private val data: NodeCutData) : Transferable, ClipboardOwner {
    companion object {
        val dataFlavor = DataFlavor(NodeCopyData::class.java, NodeCopyData::class.java.simpleName)
    }

    private val dataFlavors = arrayOf(dataFlavor, stringFlavor)

    override fun getTransferDataFlavors(): Array<DataFlavor> = dataFlavors

    override fun isDataFlavorSupported(flavor: DataFlavor?) =
        dataFlavor.mimeType == flavor?.mimeType || flavor == stringFlavor

    override fun getTransferData(flavor: DataFlavor): Any {
        if (!isDataFlavorSupported(flavor)) {
            throw UnsupportedFlavorException(flavor)
        }
        if (stringFlavor == flavor) {
            return data.nodeInfo.name
        }
        return data
    }

    override fun lostOwnership(clipboard: Clipboard?, contents: Transferable?) {}
}
package dev.priporov.ideanotes.tree.action

import com.intellij.ide.impl.DataManagerImpl
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.NodeCopyData
import dev.priporov.ideanotes.tree.node.dto.NodeCutData
import java.awt.Toolkit
import java.awt.datatransfer.*
import java.awt.datatransfer.DataFlavor.stringFlavor

class CutNodeAction(
    private val tree: BaseTree<*>,
    value: String? = null
) : AnAction(value, "", Icons.PopUpMenu.CUT_ICON) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.lastSelectedPathComponent as? NoteNode ?: return

        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val clipboardOwner = ClipboardOwner { _, _ -> }

        val transferable = NodeCutTransferable(toNodeCutData(node, tree))

        clipboard.setContents(transferable, clipboardOwner)
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    private fun readFileContentByteArray(virtualFile: VirtualFile?): ByteArray {
        if (virtualFile == null) {
            return ByteArray(0)
        }
        val project = DataManagerImpl.getInstance().getDataContext(tree).getData(CommonDataKeys.PROJECT)!!
        val file = PsiManager.getInstance(project).findFile(virtualFile)
        if (file == null) {
            return ByteArray(0)
        }
        return file.text.encodeToByteArray()
    }

    private fun toNodeCutData(node: NoteNode, tree: BaseTree<*>): NodeCutData {
        return NodeCutData().apply {
            id = node.id
            name = node.name
            type = node.type
            content = readFileContentByteArray(node.file)
            children = node.children().asSequence().mapNotNull { toNodeCutData(it as NoteNode, tree) }.toList()
            this.tree = tree
        }
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
            return data.name!!
        }
        return data
    }

    override fun lostOwnership(clipboard: Clipboard?, contents: Transferable?) {}
}
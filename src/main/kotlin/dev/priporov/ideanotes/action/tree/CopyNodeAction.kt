package dev.priporov.ideanotes.action.tree

import com.intellij.ide.impl.DataManagerImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import dev.priporov.ideanotes.dto.NodeCopyData
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.state.NodeInfo
import dev.priporov.ideanotes.util.IconUtils
import java.awt.Toolkit
import java.awt.datatransfer.*
import java.awt.datatransfer.DataFlavor.stringFlavor

class CopyNodeAction(
    private val tree: NoteTree,
    value: String? = null
) : AnAction(value, "", IconUtils.toIcon("menu/copy.png")) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.lastSelectedPathComponent as? FileTreeNode ?: return

        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val clipboardOwner = ClipboardOwner { _, _ -> }

        val copyData = NodeCopyData(NodeInfo(node), readFileContentBYteArray(node.getFile()))

        val transferable = NodeCopyTransferable(copyData)

        clipboard.setContents(transferable, clipboardOwner)
    }

    private fun readFileContentBYteArray(virtualFile: VirtualFile?): ByteArray {
        if (virtualFile == null) {
            return ByteArray(0)
        }
        val project = DataManagerImpl.getInstance().getDataContext(tree).getData(CommonDataKeys.PROJECT)!!
        val file = PsiManager.getInstance(project).findFile(virtualFile)!!

        return file.text.encodeToByteArray()
    }

}

class NodeCopyTransferable(private val data: NodeCopyData) : Transferable, ClipboardOwner {
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
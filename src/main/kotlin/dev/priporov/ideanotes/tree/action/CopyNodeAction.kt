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
import java.awt.Toolkit
import java.awt.datatransfer.*
import java.awt.datatransfer.DataFlavor.javaFileListFlavor
import java.awt.datatransfer.DataFlavor.stringFlavor
import java.io.File

class CopyNodeAction(
    private val tree: BaseTree<*>,
    value: String? = null
) : AnAction(value, "", Icons.PopUpMenu.COPY_ICON) {

    override fun actionPerformed(e: AnActionEvent) {
        val node = tree.lastSelectedPathComponent as? NoteNode ?: return

        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val clipboardOwner = ClipboardOwner { _, _ -> }

        val copyData = toNodeCopyData(node)

        val transferable = NodeCopyTransferable(copyData)

        clipboard.setContents(transferable, clipboardOwner)
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    private fun readFileContentByteArray(virtualFile: VirtualFile?): ByteArray {
        if (virtualFile == null) {
            return ByteArray(0)
        }
        val project = DataManagerImpl.getInstance().getDataContext(tree).getData(CommonDataKeys.PROJECT)!!
        val file = PsiManager.getInstance(project).findFile(virtualFile)!!

        return file.text.encodeToByteArray()
    }

    private fun toNodeCopyData(node: NoteNode): NodeCopyData {
        return NodeCopyData().apply {
            name = node.name
            id = node.id
            type = node.type
            content = readFileContentByteArray(node.file)
        }
    }
}

class NodeCopyTransferable(private val data: NodeCopyData) : Transferable, ClipboardOwner {
    companion object {
        val dataFlavor = DataFlavor(NodeCopyData::class.java, NodeCopyData::class.java.simpleName)
    }

    private val dataFlavors = arrayOf(dataFlavor, stringFlavor, javaFileListFlavor)

    override fun getTransferDataFlavors(): Array<DataFlavor> = dataFlavors

    override fun isDataFlavorSupported(flavor: DataFlavor?) =
        dataFlavor.mimeType == flavor?.mimeType || flavor == stringFlavor || flavor == javaFileListFlavor

    override fun getTransferData(flavor: DataFlavor): Any {
        if (!isDataFlavorSupported(flavor)) {
            throw UnsupportedFlavorException(flavor)
        }
        if (stringFlavor == flavor) {
            return data.name!!
        }
        if (javaFileListFlavor == flavor) {
            return listOf(
                File("${data.name}.${data.type?.extension}").also {
                    val content = data.content
                    if (content != null) {
                        it.writeBytes(data.content!!)
                    }
                }
            )
        }
        return data
    }

    override fun lostOwnership(clipboard: Clipboard?, contents: Transferable?) {}
}
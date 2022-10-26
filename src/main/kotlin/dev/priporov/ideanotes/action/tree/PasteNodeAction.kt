package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.dto.NodeCopyData
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.dto.NodeCutData
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.WriteActionUtils
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard

class PasteNodeAction(
    private val tree: NoteTree,
    private val targetNode: FileTreeNode?,
    value: String
) : AnAction(value) {

    override fun actionPerformed(e: AnActionEvent) {
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        if (!isCopyOrCut(clipboard)) {
            return
        }
        val data = clipboard.getData(NodeCopyTransferable.dataFlavor)
        if (data == null) {
            return
        }
        if (data is NodeCopyData) {
            val creationInfo = NodeCreationInfo(
                targetNode ?: tree.root,
                data.nodeInfo.name,
                data.nodeInfo.extension
            )
            val copiedNode = tree.insert(creationInfo)

            WriteActionUtils.runWriteAction {
                copiedNode.getFile()?.setBinaryContent(data.content)
            }
        }
        if (data is NodeCutData) {
            createCutNode(data, targetNode)
        }
    }

    private fun createCutNode(data: NodeCutData, targetNode: FileTreeNode?) {
        val creationInfo = NodeCreationInfo(
            targetNode ?: tree.root,
            data.nodeInfo.name,
            data.nodeInfo.extension
        )
        val copiedNode = tree.insert(creationInfo)
        WriteActionUtils.runWriteAction {
            copiedNode.getFile()?.setBinaryContent(data.content)
        }

        data.children?.forEach {
            createCutNode(it, copiedNode)
        }
    }

    private fun isCopyOrCut(clipboard: Clipboard) =
        clipboard.isDataFlavorAvailable(NodeCopyTransferable.dataFlavor) ||
                clipboard.isDataFlavorAvailable(NodeCutTransferable.dataFlavor)

}
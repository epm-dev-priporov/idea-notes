package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.priporov.ideanotes.dto.NodeCopyData
import dev.priporov.ideanotes.dto.NodeCreationInfo
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
        if (!clipboard.isDataFlavorAvailable(NodeCopyTransferable.dataFlavor)) {
            return
        }
        val nodeCopyData = clipboard.getData(NodeCopyTransferable.dataFlavor) as? NodeCopyData ?: return
//        nodeCopyData.nodeInfo.nodes.clear()
        val creationInfo = NodeCreationInfo(
            targetNode ?: tree.root,
            nodeCopyData.nodeInfo.name,
            nodeCopyData.nodeInfo.extension
        )
        val copiedNode = tree.insert(creationInfo)

        WriteActionUtils.runWriteAction {
            copiedNode.getFile()?.setBinaryContent(nodeCopyData.content)
        }
    }

}
package dev.priporov.ideanotes.tree.action

import com.intellij.ide.impl.DataManagerImpl
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiManager
import dev.priporov.ideanotes.icon.Icons
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto
import dev.priporov.ideanotes.tree.node.dto.NodeCopyData
import dev.priporov.ideanotes.tree.node.dto.NodeCutData
import dev.priporov.ideanotes.tree.node.dto.NodeType
import dev.priporov.ideanotes.tree.service.createVirtualFile
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.io.File

class PasteNodeAction(
    private val tree: BaseTree<*>,
    value: String? = null
) : AnAction(value, "", Icons.PopUpMenu.PASTE_ICON) {

    override fun actionPerformed(e: AnActionEvent) {
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard

        val selectedNode = tree.getSelectedNode()

        if (!isCopyOrCut(clipboard)) {
            if (isCopiedSystemFile(clipboard)) {
                if (clipboard.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                    val files = clipboard.getData(DataFlavor.javaFileListFlavor)
                    if (null != files && files is ArrayList<*> || files.javaClass.canonicalName == "java.util.Arrays.ArrayList") {
                        val copiedFiles = files as List<File?>
                        if (copiedFiles.isEmpty()) {
                            return
                        }

                        val file = copiedFiles[0]!!
                        val type = NodeType.fromExtension(file.extension)
                        if (type == NodeType.UNKNOWN) {
                            return
                        }

                        val project = DataManagerImpl.getInstance().getDataContext(tree).getData(CommonDataKeys.PROJECT)
                        val virtualFile = createVirtualFile(file)
                        val content: ByteArray = if (project != null) {
                            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return
                            if (psiFile.fileType.name == "Image" || psiFile.fileType.name == "MyNativeFile") {
                                virtualFile.contentsToByteArray()
                            } else if (psiFile.fileType.name != "Native") {
                                val text = psiFile.text.encodeToByteArray()
                                if (text == null || text.isEmpty()) {
                                    virtualFile.contentsToByteArray()
                                } else text
                            } else {
                                file.readBytes()
                            }
                        } else file.readBytes()

                        val createNodeDto = CreateNodeDto().apply {
                            this.name = file.name
                            this.type = type
                            this.content = content
                        }
                        val targetNode = selectedNode ?: tree.model.root as NoteNode
                        tree.createInto(createNodeDto, targetNode)
                    }
                }
            }
            return
        }

        val nodeDto = clipboard.getData(NodeCopyTransferable.dataFlavor)
        if (nodeDto == null) {
            return
        }

        if (nodeDto is NodeCopyData) {
            val targetNode: NoteNode = selectedNode ?: tree.model.root as NoteNode
            val createNodeDto = CreateNodeDto().apply {
                name = nodeDto.name
                type = nodeDto.type!!
                content = nodeDto.content
            }
            tree.createInto(createNodeDto, targetNode)
        } else if (nodeDto is NodeCutData) {
            val dto = CreateNodeDto().apply {
                this.name = nodeDto.name
                this.type = nodeDto.type
                this.content = nodeDto.content
            }
            val targetNode = if (selectedNode != null && selectedNode.id != nodeDto.id) selectedNode else tree.model.root as NoteNode
            tree.createInto(dto, targetNode)
            tree.delete(nodeDto.id!!)
        }

    }

    private fun isCopyOrCut(clipboard: Clipboard): Boolean {
        val availableDataFlavors = clipboard.availableDataFlavors
        return availableDataFlavors.contains(NodeCutTransferable.dataFlavor) || availableDataFlavors.contains(
            NodeCopyTransferable.dataFlavor
        )
    }

    private fun isCopiedSystemFile(clipboard: Clipboard): Boolean {
        return clipboard.availableDataFlavors.contains(DataFlavor.javaFileListFlavor)
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

}
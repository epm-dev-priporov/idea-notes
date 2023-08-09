package dev.priporov.ideanotes.tree

import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.ide.impl.DataManagerImpl
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileTypes.NativeFileType
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.action.tree.*
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.dto.NodeSoftLinkCreationInfo
import dev.priporov.ideanotes.tree.common.NodeType
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.node.RootFileTreeNode
import dev.priporov.ideanotes.tree.state.StateService
import dev.priporov.ideanotes.util.WriteActionUtils
import java.util.*
import javax.swing.DropMode
import javax.swing.tree.DefaultTreeModel


class NoteTree : Tree() {
    var root: FileTreeNode = service<RootFileTreeNode>()

    private var stateService = service<StateService>()

    init {
        getDefaultTreeModel().setRoot(root)
        setDropMode(DropMode.ON_OR_INSERT)
        dragEnabled = true
        isRootVisible = false
        initKeys()
    }

    fun insert(info: NodeCreationInfo): FileTreeNode {
        return insert(info.targetNode, FileTreeNode(info))
    }

    fun insert(nodeCreationInfo: NodeSoftLinkCreationInfo) {
        val targetNode = nodeCreationInfo.targetNode
        val index = targetNode.childCount
        val fileTreeNode = FileTreeNode(nodeCreationInfo)
        targetNode.insert(fileTreeNode, index)

        stateService.saveNodeInfo(fileTreeNode)
        stateService.updateOrder(targetNode)
        val expandedNodes = getExpandedNodes(targetNode)
        getDefaultTreeModel().reload(targetNode)
        expandAllNodes(expandedNodes)
    }

    fun insert(targetNode: FileTreeNode, fileTreeNode: FileTreeNode): FileTreeNode {
        val index = targetNode.childCount
        targetNode.insert(fileTreeNode, index)

        stateService.saveNodeInfo(fileTreeNode)
        stateService.updateOrder(targetNode)
        val expandedNodes = getExpandedNodes(targetNode)
        getDefaultTreeModel().reload(targetNode)
        expandAllNodes(expandedNodes)

        return fileTreeNode
    }

    fun updateOrderOf(node: FileTreeNode) {
        stateService.updateOrder(node)
    }

    fun renameNode(newName: String, node: FileTreeNode) {
        val parent = node.parent as FileTreeNode

        stateService.removeNodeInfo(node)
        WriteActionUtils.runWriteAction { node.rename(newName) }
        stateService.saveNodeInfo(node)
        stateService.updateOrder(parent)
        stateService.updateOrder(node)

        val expandedNodes = getExpandedNodes(parent)
        getDefaultTreeModel().reload(parent)
        expandAllNodes(expandedNodes)
    }

    fun delete(node: FileTreeNode) {
        val parent = node.parent
        WriteActionUtils.runWriteAction { node.delete() }
        stateService.removeNodeInfo(node)
        stateService.updateOrder(parent as FileTreeNode)
        val expandedNodes = getExpandedNodes(parent)
        getDefaultTreeModel().reload(parent)
        expandAllNodes(expandedNodes)
    }

    fun openInEditor(node: FileTreeNode?) {
        val file = node?.getFile() ?: return
        val project = DataManagerImpl.getInstance().getDataContext(this).getData(CommonDataKeys.PROJECT)!!
        if (file.extension == NodeType.DOC.extension || file.extension == NodeType.DOCX.extension) {
            NativeFileType.openAssociatedApplication(file)
        } else if (file.extension == NodeType.PDF.extension) {
            BrowserLauncher.instance.browse(file.url)
        } else if (file.extension == NodeType.EXCEL.extension) {
            NativeFileType.openAssociatedApplication(file)
        } else {
            FileEditorManager.getInstance(project).openTextEditor(
                OpenFileDescriptor(project, file, 0, 0, false),
                true
            )
        }
    }

    fun getSelectedNode(): FileTreeNode? {
        return selectionPath?.lastPathComponent as? FileTreeNode
    }

    private fun expandAllNodes(expandedNodes: ArrayList<FileTreeNode>) {
        expandedNodes.forEach { expandPath(TreeUtil.getPath(root, it)) }
    }

    private fun initKeys() {
        // add popup for 'ShowPopupMenu' shortcut, like mouse right click button
        ActionUtil.getShortcutSet("ShowPopupMenu").shortcuts.also { shortcutSet ->
            ShowTreePopUpMenuAction(this).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), this)
        }
        ActionUtil.getShortcutSet("\$Copy").shortcuts.also { shortcutSet ->
            CopyNodeAction(this).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), this)
        }
        ActionUtil.getShortcutSet("\$Cut").shortcuts.also { shortcutSet ->
            CutNodeAction(this).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), this)
        }
        ActionUtil.getShortcutSet("\$Paste").shortcuts.also { shortcutSet ->
            PasteNodeAction(this).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), this)
        }
        ActionUtil.getShortcutSet("RenameElement").shortcuts.also { shortcutSet ->
            RenameNodeAction(this).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), this)
        }
    }

    fun getDefaultTreeModel() = model as DefaultTreeModel

    private fun getExpandedNodes(node: FileTreeNode): ArrayList<FileTreeNode> {
        val list = LinkedList<FileTreeNode>()
        val expandedNodes = ArrayList<FileTreeNode>()

        list.add(node)
        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isExpanded(TreeUtil.getPath(root, treeNode))) {
                expandedNodes.add(treeNode)
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as FileTreeNode })
        }
        return expandedNodes
    }

    fun expandAll() {
        val list = LinkedList<FileTreeNode>()
        list.add(root)
        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isCollapsed(TreeUtil.getPath(root, treeNode))) {
                expandPath(TreeUtil.getPath(root, treeNode))
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as FileTreeNode })
        }
    }

    fun collapseAll() {
        getExpandedNodes(root).asSequence().filter { it != root }.forEach {
            collapsePath(TreeUtil.getPath(it.parent, it))
        }
    }

}

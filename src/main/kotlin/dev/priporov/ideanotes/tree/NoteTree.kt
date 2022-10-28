package dev.priporov.ideanotes.tree

import com.intellij.ide.impl.DataManagerImpl
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.action.tree.*
import dev.priporov.ideanotes.dto.NodeCreationInfo
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
        val targetNode = info.targetNode
        val index = targetNode.childCount
        val fileTreeNode = FileTreeNode(info)

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
        val project = getProject() ?: ProjectManager.getInstance().openProjects[0]

        FileEditorManager.getInstance(project).openTextEditor(
            OpenFileDescriptor(project, file, 0, 0, false),
            true
        )
    }

    fun getSelectedNode(): FileTreeNode? {
        return selectionPath?.lastPathComponent as? FileTreeNode
    }

    private fun expandAllNodes(expandedNodes: ArrayList<FileTreeNode>) {
        expandedNodes.forEach { expandPath(TreeUtil.getPath(root, it)) }
    }

    private fun initKeys() {
        // add popup for 'ShowPopupMenu' shortcut, like mouse right click button
        ActionUtil.getShortcutSet("ShowPopupMenu").shortcuts.forEach { shortcut ->
            ShowTreePopUpMenuAction(this).registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
        ActionUtil.getShortcutSet("\$Copy").shortcuts.forEach { shortcut ->
            CopyNodeAction(this).registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
        ActionUtil.getShortcutSet("\$Cut").shortcuts.forEach { shortcut ->
            CutNodeAction(this).registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
        ActionUtil.getShortcutSet("\$Paste").shortcuts.forEach { shortcut ->
            PasteNodeAction(this).registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
        ActionUtil.getShortcutSet("RenameElement").shortcuts.forEach { shortcut ->
            RenameNodeAction(this).registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
    }

    private fun getProject() = DataManagerImpl.getInstance().getDataContext(this).getData(CommonDataKeys.PROJECT)

    private fun getDefaultTreeModel() = model as DefaultTreeModel

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
}

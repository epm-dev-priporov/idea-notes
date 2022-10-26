package dev.priporov.ideanotes.tree

import com.intellij.ide.impl.DataManagerImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.treeStructure.Tree
import dev.priporov.ideanotes.action.tree.CopyNodeAction
import dev.priporov.ideanotes.action.tree.CutNodeAction
import dev.priporov.ideanotes.action.tree.RenameNodeAction
import dev.priporov.ideanotes.action.tree.ShowTreePopUpMenuAction
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.node.RootFileTreeNode
import dev.priporov.ideanotes.tree.state.StateService
import dev.priporov.ideanotes.util.WriteActionUtils
import javax.swing.DropMode
import javax.swing.tree.DefaultTreeModel


class NoteTree : Tree() {
    var root: FileTreeNode = service<RootFileTreeNode>()

    private var stateService = service<StateService>()

    init {
        StoragePathMacros.WORKSPACE_FILE
        getDefaultTreeModel().setRoot(root)
        setDropMode(DropMode.ON_OR_INSERT)
        dragEnabled = true
        isRootVisible = false
        initKeys()
    }

    fun insert(info: NodeCreationInfo): FileTreeNode {
        val targetNode = info.targetNode
        val index = targetNode.childCount
        val fileTreeNode = FileTreeNode(info).also {
            it.parentId = targetNode.id
        }

        targetNode.insert(fileTreeNode, index)

        stateService.saveNodeInfo(fileTreeNode)
        stateService.updateOrder(targetNode)

        getDefaultTreeModel().reload(targetNode)

        return fileTreeNode
    }

    fun updateOrderOf(node: FileTreeNode) {
        stateService.updateOrder(node)
    }

    private fun getDefaultTreeModel() = model as DefaultTreeModel

    fun renameNode(newName: String, node: FileTreeNode) {
        val parent = node.parent as FileTreeNode

        stateService.removeNodeInfo(node)
        WriteActionUtils.runWriteAction { node.rename(newName) }
        stateService.saveNodeInfo(node)
        stateService.updateOrder(parent)

        getDefaultTreeModel().reload(parent)
    }

    fun delete(node: FileTreeNode) {
        val parent = node.parent
        WriteActionUtils.runWriteAction { node.delete() }
        stateService.removeNodeInfo(node)
        stateService.updateOrder(parent as FileTreeNode)
        getDefaultTreeModel().reload(parent)
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

    private fun initKeys() {
        // add popup for 'ShowPopupMenu' shortcut, like mouse right click button
        ActionUtil.getShortcutSet("ShowPopupMenu").shortcuts.forEach { shortcut ->
            ShowTreePopUpMenuAction(this).registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
        ActionUtil.getShortcutSet("\$Copy").shortcuts.forEach { shortcut ->
            CopyNodeAction(this, "").registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
        ActionUtil.getShortcutSet("\$Cut").shortcuts.forEach { shortcut ->
            CutNodeAction(this, "").registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
        ActionUtil.getShortcutSet("RenameElement").shortcuts.forEach { shortcut ->
            RenameNodeAction(this,"Rename").registerCustomShortcutSet(CustomShortcutSet(shortcut), this)
        }
    }

    private fun getProject() = DataManagerImpl.getInstance().getDataContext(this).getData(CommonDataKeys.PROJECT)
}

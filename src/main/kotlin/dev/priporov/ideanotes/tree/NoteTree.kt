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
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.action.tree.*
import dev.priporov.ideanotes.dialog.NATIVE
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

    object State {
        var init = false
    }

    private var stateService = service<StateService>()
    private var filesToAddAfterInit = ArrayList<VirtualFile>()

    init {
        getDefaultTreeModel().setRoot(root)
        setDropMode(DropMode.ON_OR_INSERT)
        dragEnabled = true
        isRootVisible = false
        initKeys()
    }

    fun addAfterInitialization(virtualFile: VirtualFile) {
        if (!State.init) {
            filesToAddAfterInit.add(virtualFile)
        } else {
            val project = ProjectManager.getInstance().openProjects.firstOrNull()
            if (project == null) {
                return
            }
            insert(virtualFile).apply {
                val file = PsiManager.getInstance(project).findFile(virtualFile)!!
                var content = if (file.fileType.name == "Image") {
                    virtualFile.contentsToByteArray()
                } else {
                    file.text.encodeToByteArray()
                }

                setData(content)
            }
        }
    }

    fun insertFilesFromQueue() {
        State.init = true

        val project = ProjectManager.getInstance().openProjects.firstOrNull()
        if (project == null) {
            return
        }
        filesToAddAfterInit.forEach { virtualFile ->
            insert(virtualFile).apply {
                val file = PsiManager.getInstance(project).findFile(virtualFile)!!
                var content = if (file.fileType.name == "Image") {
                    virtualFile.contentsToByteArray()
                } else {
                    file.text.encodeToByteArray()
                }

                setData(content)
            }
        }
        filesToAddAfterInit.clear()
    }

    fun insert(virtualFile: VirtualFile): FileTreeNode {
        return insert(NodeCreationInfo(root, virtualFile.nameWithoutExtension, virtualFile.extension!!))
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

    // TODO wrong remove of the nested notes
    fun delete(node: FileTreeNode) {
        val parent = node.parent

        var children = getChildren(node)

        var queue = ArrayDeque<FileTreeNode>()
        val toRemove = ArrayList<FileTreeNode>()

        queue.addAll(children)
        toRemove.addAll(children)

        while (queue.isNotEmpty()) {
            val treeNode = queue.pop();
            children = getChildren(treeNode)
            queue.addAll(children)
            toRemove.addAll(children)
        }
        toRemove.forEach { childNode ->
            stateService.removeNodeInfo(childNode)
            stateService.getTreeState().getOrder().remove(childNode.id)
        }

        WriteActionUtils.runWriteAction { node.delete() }
        stateService.removeNodeInfo(node)
        stateService.getTreeState().getOrder().remove(node.id)

        stateService.updateOrder(parent as FileTreeNode)
        val expandedNodes = getExpandedNodes(parent)
        getDefaultTreeModel().reload(parent)
        expandAllNodes(expandedNodes)
    }

    private fun getChildren(node: FileTreeNode) = node.children()
        .asSequence()
        .map { it as FileTreeNode }
        .toMutableList()

    fun openInEditor(node: FileTreeNode?) {
        val file = node?.getFile() ?: return
        val project = DataManagerImpl.getInstance().getDataContext(this).getData(CommonDataKeys.PROJECT)!!
        if (file.extension == NodeType.DOC.extension || file.extension == NodeType.DOCX.extension) {
            NativeFileType.openAssociatedApplication(file)
        } else if (file.extension == NodeType.PDF.extension) {
            BrowserLauncher.instance.browse(file.url)
        } else if (file.extension == NodeType.CSV.extension) {
            openFile(file, project, NodeType.CSV)
        } else if (file.extension == NodeType.EXCEL.extension) {
            openFile(file, project, NodeType.EXCEL)
        } else {
            FileEditorManager.getInstance(project).openTextEditor(
                OpenFileDescriptor(project, file, 0, 0, false),
                true
            )
        }
    }

    private fun openFile(file: VirtualFile, project: Project, type: NodeType) {
        val readerType = service<StateService>().state.getReaderType(type)
        if (readerType == null || readerType.equals(NATIVE)) {
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

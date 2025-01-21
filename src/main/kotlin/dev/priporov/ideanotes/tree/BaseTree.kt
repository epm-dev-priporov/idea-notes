package dev.priporov.ideanotes.tree

import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.ide.impl.DataManagerImpl
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileTypes.NativeFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.factory.NoteNodeFactory
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.node.dto.CreateNodeDto
import dev.priporov.ideanotes.tree.node.dto.NodeType
import dev.priporov.ideanotes.tree.node.mapper.CreateDtoToTreeNodeMapper
import java.util.*
import javax.swing.tree.DefaultTreeModel

abstract class BaseTree<T : DefaultTreeModel> : Tree() {
    protected val nodesGroupedById = HashMap<String, NoteNode>()

    abstract fun createNewInRoot(createNodeDto: CreateNodeDto): NoteNode

    open fun renameNode(name: String, node: NoteNode) {
        service<NoteNodeFactory>().rename(name, node)
    }

    open fun createInto(createNodeDto: CreateNodeDto, targetNode: NoteNode): NoteNode {
        val node: NoteNode = service<CreateDtoToTreeNodeMapper>().toNoteNode(createNodeDto)
        val expandedNodes = getExpandedNodes(targetNode)

        targetNode.insert(
            node,
            targetNode.childCount
        )

        getTreeModel().reload(targetNode)
        expandNodes(expandedNodes)
        nodesGroupedById[node.id!!] = node

        return node
    }

    fun expandAll() {
        val list = LinkedList<NoteNode>()
        val root = model.root as NoteNode

        list.add(root)

        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isCollapsed(TreeUtil.getPath(root, treeNode))) {
                expandPath(TreeUtil.getPath(root, treeNode))
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as NoteNode })
        }
    }

    fun collapseAll() {
        val root = getRoot()

        getExpandedNodes(root).asSequence().filter { it != root }.forEach {
            collapsePath(TreeUtil.getPath(it.parent, it))
        }
    }

    fun getSelectedNode(): NoteNode? = selectionPath?.lastPathComponent as? NoteNode

    fun openInEditor(node: NoteNode?) {
        val file = node?.file ?: return
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
//        val readerType = service<StateService>().state.getReaderType(type)
//        if (readerType == null || readerType.equals(NATIVE)) {
//            NativeFileType.openAssociatedApplication(file)
//        } else {
        FileEditorManager.getInstance(project).openTextEditor(
            OpenFileDescriptor(project, file, 0, 0, false),
            true
        )
//        }
    }

    protected fun getRoot() = getTreeModel().root as NoteNode

    protected fun getTreeModel() = model as T

    protected fun getExpandedNodes(node: NoteNode): ArrayList<NoteNode> {
        val list = LinkedList<NoteNode>()
        val expandedNodes = ArrayList<NoteNode>()
        val root = getRoot()

        list.add(node)
        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isExpanded(TreeUtil.getPath(root, treeNode))) {
                expandedNodes.add(treeNode)
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as NoteNode })
        }
        return expandedNodes
    }

    protected fun expandNodes(expandedNodes: ArrayList<NoteNode>) {
        expandedNodes.forEach { expandPath(TreeUtil.getPath(getRoot(), it)) }
    }

    abstract fun delete(id: String)

}
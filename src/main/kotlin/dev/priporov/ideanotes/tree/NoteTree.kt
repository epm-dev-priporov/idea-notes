package dev.priporov.ideanotes.tree

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import dev.priporov.ideanotes.tree.model.NoteTreeModel
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.tree.node.RootNode
import java.util.*


@Service(Service.Level.PROJECT)
class NoteTree : Tree() {
    init {
        model = service<NoteTreeModel>()

        isRootVisible = false

        // TODO
        model.root.insert(FileTreeNode("test"), 0)
        model.root.insert(FileTreeNode("test2"), 1)
        model.root.insert(FileTreeNode("test3"), 1)

        expandAll()
    }


    override fun getModel(): NoteTreeModel = service<NoteTreeModel>()

    fun expandAll() {
        val list = LinkedList<FileTreeNode>()
        val root = service<RootNode>()
        list.add(root)

        while (list.isNotEmpty()) {
            val treeNode = list.pop()
            if (isCollapsed(TreeUtil.getPath(root, treeNode))) {
                expandPath(TreeUtil.getPath(root, treeNode))
            }
            list.addAll(treeNode.children().asSequence().mapNotNull { it as FileTreeNode })
        }
    }

}
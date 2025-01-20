package dev.priporov.ideanotes.tree.handler

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.AppNoteTree
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.ProjectNoteTree
import dev.priporov.ideanotes.tree.factory.NoteNodeFactory
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.tree.service.ApplicationTreeStateService
import dev.priporov.ideanotes.tree.service.ProjectTreeStateService
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException
import javax.swing.JComponent
import javax.swing.JTree
import javax.swing.TransferHandler
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeNode
import javax.swing.tree.TreePath

@Service
class DragAndDropTransferHandler : TransferHandler() {
    private val mimeType = "${DataFlavor.javaJVMLocalObjectMimeType};class=\"${Array<DefaultMutableTreeNode>::class.java.name}\""
    private var nodesFlavor: DataFlavor = DataFlavor(mimeType)
    private var flavors = arrayOf(nodesFlavor)
    private var nodesToRemove: Array<DefaultMutableTreeNode> = arrayOf()

    override fun canImport(support: TransferSupport): Boolean {
        if (!support.isDrop) {
            return false
        }
        support.setShowDropLocation(true)
        if (!support.isDataFlavorSupported(nodesFlavor)) {
            return false
        }
        // Do not allow a drop on the drag source selections.
        val dl = support.dropLocation as JTree.DropLocation
        val tree = support.component as JTree
        val dropRow = tree.getRowForPath(dl.path)
        val selRows = tree.selectionRows
        for (i in selRows.indices) {
            if (selRows[i] == dropRow) {
                return false
            }
        }
        return true
    }

    override fun createTransferable(tree: JComponent): NodesTransferable? {
        val tree = tree as JTree
        val paths = tree.selectionPaths ?: return null
        // Make up a node array of copies for transfer and
        // another for/of the nodes that will be removed in
        // exportDone after a successful drop.
        val copies: MutableList<DefaultMutableTreeNode> = ArrayList()
        val toRemove: MutableList<DefaultMutableTreeNode> = ArrayList()
        val firstNode = paths[0].lastPathComponent as NoteNode
        val doneItems: HashSet<TreeNode> = LinkedHashSet(paths.size)
        val copy = copy(firstNode, doneItems, tree)
        copies.add(copy)
        toRemove.add(firstNode)
        for (i in 1 until paths.size) {
            val next = paths[i].lastPathComponent as NoteNode
            if (doneItems.contains(next)) {
                continue
            }
            // Do not allow higher level nodes to be added to list.
            if (next.level < firstNode.level) {
                break
            } else if (next.level > firstNode.level) {  // child node
                copy.add(copy(next, doneItems, tree))
                // node already contains child
            } else {                                        // sibling
                copies.add(copy(next, doneItems, tree))
                toRemove.add(next)
            }
            doneItems.add(next)
        }
        val nodes = copies.toTypedArray()
        nodesToRemove = toRemove.toTypedArray()
        return NodesTransferable(nodes)
    }

    private fun copy(
        node: NoteNode,
        doneItems: HashSet<TreeNode>,
        tree: JTree
    ): DefaultMutableTreeNode {
        val copy = service<NoteNodeFactory>().copy(node)
        doneItems.add(node)
        for (i in 0 until node.childCount) {
            copy.add(copy(node.getChildAt(i) as NoteNode, doneItems, tree))
        }
        val row = tree.getRowForPath(TreePath(copy.path))
        tree.expandRow(row)

        return copy
    }

    override fun exportDone(source: JComponent, data: Transferable, action: Int) {
        if (action and MOVE == MOVE) {
            val tree = source as BaseTree<*>
            val model = tree.model as DefaultTreeModel
            // Remove nodes saved in nodesToRemove in createTransferable.
            for (i in nodesToRemove.indices) {
                val node = nodesToRemove[i]
                val parent = node.parent as NoteNode
                model.removeNodeFromParent(node)
                getTreeState(tree)?.updateNodeOrder(parent)
            }
        }
    }

    override fun getSourceActions(c: JComponent): Int {
        return MOVE
    }

    override fun importData(support: TransferSupport): Boolean {
        if (!canImport(support)) {
            return false
        }
        // Extract transfer data.
        var nodes: Array<DefaultMutableTreeNode?>? = null
        try {
            val t = support.transferable
            nodes = t.getTransferData(nodesFlavor) as Array<DefaultMutableTreeNode?>
        } catch (ufe: UnsupportedFlavorException) {
            println("UnsupportedFlavor: " + ufe.message)
        } catch (ioe: IOException) {
            println("I/O error: " + ioe.message)
        }
        // Get drop location info.
        val dl = support.dropLocation as JTree.DropLocation
        val childIndex = dl.childIndex
        val dest = dl.path
        val parent = dest.lastPathComponent as DefaultMutableTreeNode
        val tree = support.component as BaseTree<*>
        val model = tree.model as DefaultTreeModel
        // Configure for drop mode.
        var index = childIndex // DropMode.INSERT
        if (childIndex == -1) {     // DropMode.ON
            index = parent.childCount
        }
        // Add data to model.
        for (i in nodes!!.indices) {
            val treeNode = nodes[i]
            model.insertNodeInto(treeNode, parent, index)
//            service<VirtualFileContainer>().addNode(treeNode as NoteNode) TODO

//            treeNode.editor?.also { editor -> TODO
//                OpenedFileListener.applyAction(editor, service<VirtualFileContainer>())
//            }

            getTreeState(tree)?.updateNodeOrder(parent as NoteNode)

            index++
            println(index)
        }
        return true
    }

    private fun getTreeState(tree: BaseTree<*>) = when (tree) {
        is AppNoteTree -> service<ApplicationTreeStateService>()
        is ProjectNoteTree -> service<ProjectTreeStateService>()
        else -> null
    }

    override fun toString(): String {
        return javaClass.name
    }

    inner class NodesTransferable(private var nodes: Array<DefaultMutableTreeNode>) : Transferable {
        @Throws(UnsupportedFlavorException::class)
        override fun getTransferData(flavor: DataFlavor): Any {
            if (!isDataFlavorSupported(flavor)) {
                throw UnsupportedFlavorException(flavor)
            }
            return nodes
        }

        override fun getTransferDataFlavors(): Array<DataFlavor> {
            return flavors
        }

        override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
            return nodesFlavor.equals(flavor)
        }
    }


}



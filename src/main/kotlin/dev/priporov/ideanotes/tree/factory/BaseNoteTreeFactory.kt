package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.handler.DragAndDropTransferHandler
import dev.priporov.ideanotes.tree.listener.TreeMouseListener
import javax.swing.DropMode

open class BaseNoteTreeFactory<T : BaseTree<*>>() {

    protected fun init(tree: BaseTree<*>) {
        tree.addMouseListener(TreeMouseListener(tree))
        tree.expandAll()
        tree.transferHandler = service<DragAndDropTransferHandler>()
        tree.isRootVisible = false
        tree.setDropMode(DropMode.ON_OR_INSERT)
        tree.dragEnabled = true
    }

}
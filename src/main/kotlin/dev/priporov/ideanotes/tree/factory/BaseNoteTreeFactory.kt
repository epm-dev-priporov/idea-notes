package dev.priporov.ideanotes.tree.factory

import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.listener.TreeMouseListener

open class BaseNoteTreeFactory<T : BaseTree<*>>() {

    protected fun init(tree: BaseTree<*>) {
        tree.addMouseListener(TreeMouseListener(tree))
        tree.expandAll()
    }

}
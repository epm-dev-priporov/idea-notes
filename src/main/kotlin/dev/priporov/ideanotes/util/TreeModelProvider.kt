package dev.priporov.ideanotes.util

import dev.priporov.ideanotes.tree.NoteTree
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Consumer
import javax.swing.tree.DefaultTreeModel

class TreeModelProvider {

    private var commonModel: DefaultTreeModel? = null
    private var lock = ReentrantLock()
    private var initModel: Consumer<NoteTree?>? = null
    var tree: NoteTree? = null

    fun setCommonModel(tree: NoteTree) {
        lock.lock()
        this.tree = tree
        if (commonModel == null) {
            commonModel = tree.model as DefaultTreeModel
        } else {
            tree.model = commonModel
        }
        initModelOnce()

        lock.unlock()
    }

    private fun initModelOnce() {
        initModel?.accept(tree)
        initModel = null
    }

}

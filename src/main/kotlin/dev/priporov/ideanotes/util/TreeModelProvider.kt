package dev.priporov.ideanotes.util

import dev.priporov.ideanotes.tree.NoteTree
import java.util.concurrent.locks.ReentrantLock
import javax.swing.tree.DefaultTreeModel

class TreeModelProvider {

    private var commonModel: DefaultTreeModel? = null
    private var lock = ReentrantLock()
    private var initModel: Runnable? = null

    fun setCommonModel(tree: NoteTree) {
        lock.lock()
        if (commonModel == null) {
            commonModel = tree.model as DefaultTreeModel
        } else {
            tree.model = commonModel
        }
        initModelOnce()

        lock.unlock()
    }

    fun getModel() = commonModel

    fun setCallBack(function: Runnable) {
        initModel = function
    }

    private fun initModelOnce() {
        initModel?.run()
        initModel = null
    }

}

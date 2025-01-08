package dev.priporov.ideanotes.util

import dev.priporov.ideanotes.tree.NoteTree
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Consumer
import javax.swing.tree.DefaultTreeModel

class TreeModelProvider {

    var tree: NoteTree? = null

    private var commonModel: DefaultTreeModel? = null
    private var lock = ReentrantLock()

    fun setCommonModel(tree: NoteTree) {
        lock.lock()
        this.tree = tree
        if (commonModel == null) {
            commonModel = tree.model as DefaultTreeModel
        } else {
            tree.model = commonModel
        }

        lock.unlock()
    }

}

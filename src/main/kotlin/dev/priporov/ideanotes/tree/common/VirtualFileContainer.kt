package dev.priporov.ideanotes.tree.common

import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.ideanotes.util.FileNodeUtils.isInBaseDir

class VirtualFileContainer {

    private val files: MutableSet<VirtualFile> = HashSet()
    private val nodes: MutableMap<VirtualFile, FileTreeNode> = HashMap()
    private val filesForInitAction: MutableMap<VirtualFile, Runnable> = HashMap()

    fun getNode(file: VirtualFile) = nodes[file]

    fun getFiles(): Set<VirtualFile> = files

    fun addNode(node: FileTreeNode) {
        node.getFile()?.also { virtualFile ->
            files.add(virtualFile)
            nodes[virtualFile] = node
        }
    }

    fun removeFile(file: VirtualFile) {
        files.remove(file)
        nodes.remove(file)
    }

    fun isNote(file: VirtualFile) = files.contains(file)

    fun initLateIfNeeded(file: VirtualFile?, runnable: Runnable) {
        if (isInBaseDir(file) && file != null) {
            filesForInitAction[file] = runnable
        }
    }

    fun init(file: VirtualFile?) {
        filesForInitAction[file]?.run()
    }

}
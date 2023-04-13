package dev.priporov.ideanotes.tree.common

import com.intellij.openapi.vfs.VirtualFile

class VirtualFileContainer {

    private val files: MutableSet<VirtualFile> = HashSet()

    fun getFiles(): Set<VirtualFile> = files

    fun addFile(file: VirtualFile) {
        files.add(file)
    }

    fun removeFile(file: VirtualFile) {
        files.remove(file)
    }

    fun isNote(file: VirtualFile) = files.contains(file)

}
package dev.priporov.ideanotes.tree.container

import com.intellij.openapi.components.Service
import com.intellij.openapi.vfs.VirtualFile

@Service
class VirtualFileContainer {

    private val virtualFilesGroupedById: MutableMap<String, VirtualFile> = HashMap()

    fun put(id: String, virtualFile: VirtualFile) {
        virtualFilesGroupedById[id] = virtualFile
    }

    fun isNote(file: VirtualFile): Boolean = virtualFilesGroupedById.values.contains(file)
}
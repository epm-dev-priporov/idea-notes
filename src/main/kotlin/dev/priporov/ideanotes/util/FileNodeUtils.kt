package dev.priporov.ideanotes.util

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.util.*

class FileNodeUtils {
    companion object {
        private const val PLUGIN_ID = "dev.priporov.idea-notes"

        private val fileSeparator: String = System.getProperty("file.separator") ?: File.pathSeparator
        private val pluginDir = PropertiesComponent.getInstance().getValue(
            PLUGIN_ID, "${System.getProperty("user.home")}${fileSeparator}.ideanotes"
        ).run { File(this) }

        init {
            if (!pluginDir.exists()) {
                pluginDir.mkdir()
            }
        }

        fun generateNodeName(name: String?): String? {
            if (name == null) {
                return null
            }
            return "${name}_${UUID.randomUUID().toString().substring(0, 6)}"
        }

        fun initFile(id: String?, extension: String?): VirtualFile? {
            if (id == null || extension == null) {
                return null
            }
            val file = File("${pluginDir.path}${fileSeparator}${id}.${extension}")

            if (!file.exists()) {
                file.createNewFile()
            }

            return createVirtualFile(file)
        }

        private fun createVirtualFile(file: File): VirtualFile {
            val localFileSystem = LocalFileSystem
                .getInstance()
            val path = file.toPath()

            return localFileSystem.refreshAndFindFileByNioFile(path)
                ?: localFileSystem.refreshAndFindFileByIoFile(file)
                ?: localFileSystem.refreshAndFindFileByPath(path.toString())!!
        }
    }

}

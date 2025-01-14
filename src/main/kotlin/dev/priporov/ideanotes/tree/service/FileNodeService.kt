package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

val fileSeparator: String = System.getProperty("file.separator") ?: File.pathSeparator

@Service
class FileNodeService {

    fun createApplicationFile(
        id: String,
        extension: String,
        content: ByteArray? = null
    ): VirtualFile {
        val applicationState = service<ApplicationStateService>().applicationState
        val appBaseDir = applicationState.appBaseDir
        val file = File("$appBaseDir$fileSeparator$id.$extension").apply {
            createNewFile()
            if (content != null && content.isNotEmpty()) {
                writeBytes(content)
            }
        }
        return createVirtualFile(file)
    }

    fun createBaseDirIfNotExists(appBaseDir:String) {
        val baseDIr = File(appBaseDir)
        if (!baseDIr.exists()) {
            baseDIr.mkdirs()
        }
    }

    private fun createVirtualFile(file: File): VirtualFile {
        val localFileSystem = LocalFileSystem.getInstance()
        val path = file.toPath()

        return localFileSystem.refreshAndFindFileByNioFile(path)
            ?: localFileSystem.refreshAndFindFileByIoFile(file)
            ?: localFileSystem.refreshAndFindFileByPath(path.toString())!!
    }

}
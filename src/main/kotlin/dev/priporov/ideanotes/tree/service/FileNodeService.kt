package dev.priporov.ideanotes.tree.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import dev.priporov.ideanotes.tree.container.VirtualFileContainer
import dev.priporov.ideanotes.tree.node.NoteNode
import dev.priporov.ideanotes.util.WriteActionUtil
import java.io.File

val fileSeparator: String = System.getProperty("file.separator") ?: File.pathSeparator

@Service
class FileNodeService {

    fun initVirtualFile(id: String, extension: String?): VirtualFile {
        val applicationState = service<PluginStateService>().applicationState
        val appBaseDir = applicationState.appBaseDir

        val path = "$appBaseDir$fileSeparator$id${if (extension == null) "" else ".$extension"}"
        return createVirtualFile(File(path)).apply {
            service<VirtualFileContainer>().put(id, this)
        }
    }

    fun initProjectVirtualFile(id: String, extension: String?, project: Project): VirtualFile {
        val baseDir = service<PluginStateService>().getProjectBaseDir(project)

        val path = "$baseDir$fileSeparator$id${if (extension == null) "" else ".$extension"}"
        return createVirtualFile(File(path)).apply {
            service<VirtualFileContainer>().put(id, this)
        }
    }

    fun createApplicationFile(
        id: String,
        extension: String?,
        content: ByteArray? = null
    ): VirtualFile {
        val applicationState = service<PluginStateService>().applicationState
        val appBaseDir = applicationState.appBaseDir

        val file = File("$appBaseDir$fileSeparator${toFileName(id, extension)}").apply {
            createNewFile()
            if (content != null && content.isNotEmpty()) {
                writeBytes(content)
            }
        }
        return createVirtualFile(file).apply {
            service<VirtualFileContainer>().put(id, this)
        }
    }

    fun createProjectFile(
        id: String,
        extension: String?,
        project: Project,
        content: ByteArray? = null
    ): VirtualFile {
        val applicationState = service<PluginStateService>().applicationState
        val projectNoteDir = applicationState.projectNoteDir

        val file = File("${project.guessProjectDir()?.path}$fileSeparator$projectNoteDir$fileSeparator${toFileName(id, extension)}").apply {
            createNewFile()
            if (content != null && content.isNotEmpty()) {
                writeBytes(content)
            }
        }
        return createVirtualFile(file).apply {
            service<VirtualFileContainer>().put(id, this)
        }
    }

    fun createBaseDirIfNotExists(appBaseDir: String) {
        val baseDIr = File(appBaseDir)
        if (!baseDIr.exists()) {
            baseDIr.mkdirs()
        }
    }

    fun renameFile(file: VirtualFile, node: NoteNode) {
        WriteActionUtil.runWriteAction { file.rename(this, toFileName(node.id!!, node.type.extension)) }
    }

    private fun createVirtualFile(file: File): VirtualFile {
        val localFileSystem = LocalFileSystem.getInstance()
        val path = file.toPath()

        return localFileSystem.refreshAndFindFileByNioFile(path)
            ?: localFileSystem.refreshAndFindFileByIoFile(file)
            ?: localFileSystem.refreshAndFindFileByPath(path.toString())!!
    }

    private fun toFileName(id: String, extension: String?): String {
        val extensionWithDot = if (extension.isNullOrBlank()) "" else ".$extension"
        return "$id$extensionWithDot"
    }

}
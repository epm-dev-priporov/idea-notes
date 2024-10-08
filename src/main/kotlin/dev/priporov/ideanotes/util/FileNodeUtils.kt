package dev.priporov.ideanotes.util

import com.intellij.ide.impl.DataManagerImpl
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.node.FileTreeNode
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object FileNodeUtils {

    private const val PLUGIN_ID = "dev.priporov.idea-notes"

    private val logger = Logger.getInstance(FileNodeUtils::class.java)

    val fileSeparator: String = System.getProperty("file.separator") ?: File.pathSeparator

    val baseDir = PropertiesComponent.getInstance().getValue(
        PLUGIN_ID, "${System.getProperty("user.home")}${fileSeparator}.ideanotes2"
    ).run { File(this) }

    init {
        if (!baseDir.exists()) {
            baseDir.mkdir()
        }
    }

    fun isInBaseDir(file: VirtualFile?) = file?.path?.startsWith(baseDir.path, false) ?: false

    fun generateNodeName(name: String?): String? {
        if (name == null) {
            return null
        }
        return "${name}_${UUID.randomUUID().toString().substring(0, 6)}"
    }

    fun copyToNode(file: File, node: FileTreeNode) {
        WriteActionUtils.runWriteAction {
            node.getFile()?.setBinaryContent(file.readBytes())
        }
    }

    fun initFile(id: String?, extension: String?): VirtualFile? {
        if (id == null || extension == null) {
            return null
        }

        val filename = "${baseDir.path}${fileSeparator}${id}"
        val file = File("$filename.${extension}")

        if (!file.exists()) {
            file.createNewFile()
        }

        return createVirtualFile(file)
    }

    fun initSoftLink(id: String?, extension: String?, targetFile: File): VirtualFile? {
        if (id == null || extension == null) {
            return null
        }

        val filename = "${baseDir.path}${fileSeparator}${id}"
        try {
            val symbolicLink = Files.createSymbolicLink(Path.of("$filename.${extension}"), targetFile.toPath())

            return createVirtualFile(symbolicLink.toFile())
        } catch (e: Exception) {
            logger.error(e.message)
            logger.error("In Windows OS there is a permission issue: https://github.com/epm-dev-priporov/idea-notes/issues/6")
            return null
        }
    }

    fun readFileContentByteArray(tree: NoteTree, virtualFile: VirtualFile?): ByteArray {
        if (virtualFile == null) {
            return ByteArray(0)
        }
        val project = DataManagerImpl.getInstance().getDataContext(tree).getData(CommonDataKeys.PROJECT)!!
        val file = PsiManager.getInstance(project).findFile(virtualFile)!!

        return file.text.encodeToByteArray()
    }

}

fun createVirtualFile(file: File): VirtualFile {
    val localFileSystem = LocalFileSystem
        .getInstance()
    val path = file.toPath()

    return localFileSystem.refreshAndFindFileByNioFile(path)
        ?: localFileSystem.refreshAndFindFileByIoFile(file)
        ?: localFileSystem.refreshAndFindFileByPath(path.toString())!!
}


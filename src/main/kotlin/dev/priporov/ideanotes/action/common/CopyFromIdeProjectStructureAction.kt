package dev.priporov.ideanotes.action.common

import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys.SELECTED_ITEMS
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ExtensionFileHelper

class CopyFromIdeProjectStructureAction : AnAction("Copy to Notes") {

    override fun actionPerformed(event: AnActionEvent) {
        val tree: NoteTree = event.project?.getService(NoteTree::class.java) ?: return
        val data = event.getData(SELECTED_ITEMS) ?: return
        val files: List<VirtualFile> = data.asSequence()
            .filter { it is PsiFileNode }
            .map { it as PsiFileNode }
            .map { it.virtualFile }
            .filterNotNull()
            .filter { hasSupportedExtension(it) }
            .toList()

        files.forEach { virtualFile ->
            tree.insert(NodeCreationInfo(tree.root, virtualFile.nameWithoutExtension, virtualFile.extension!!)).apply {
                val file = PsiManager.getInstance(event.project!!).findFile(virtualFile)!!
                var content = if (file.fileType.name == "Image") {
                    virtualFile.contentsToByteArray()
                } else {
                    file.text.encodeToByteArray()
                }

                setData(content)
            }
        }

    }

    override fun update(e: AnActionEvent) {

        val data = e.getData(SELECTED_ITEMS)
        if (e.project?.getService(NoteTree::class.java) == null || !isSupported(data)) {
            e.presentation.isEnabledAndVisible = false
        }
    }

    private fun isSupported(psiElement: Array<Any>?): Boolean = psiElement?.asSequence()
        ?.filter { it is PsiFileNode }
        ?.map { it as PsiFileNode }
        ?.map { it.virtualFile }
        ?.any { hasSupportedExtension(it) }
        ?: false

    private fun hasSupportedExtension(file: VirtualFile?) = ExtensionFileHelper.containsExtension(file?.extension)
}
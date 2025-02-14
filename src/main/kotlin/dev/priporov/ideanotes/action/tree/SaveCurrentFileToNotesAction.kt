package dev.priporov.ideanotes.action.tree

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiManager
import dev.priporov.ideanotes.dto.NodeCreationInfo
import dev.priporov.ideanotes.tree.NoteTree

class SaveCurrentFileToNotesAction : AnAction("Copy to Notes") {
    override fun actionPerformed(event: AnActionEvent) {
        val tree: NoteTree = event.project?.getService(NoteTree::class.java) ?: return
        val virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(event.dataContext) ?: return

        tree.addAfterInitialization(virtualFile)

    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

}
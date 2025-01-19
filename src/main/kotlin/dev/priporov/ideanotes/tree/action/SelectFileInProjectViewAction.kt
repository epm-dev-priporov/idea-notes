package dev.priporov.ideanotes.tree.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class SelectFileInProjectViewAction(

) : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT

}
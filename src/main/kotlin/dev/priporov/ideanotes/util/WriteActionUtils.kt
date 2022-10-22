package dev.priporov.ideanotes.util

import com.intellij.openapi.application.ApplicationManager
import org.jetbrains.annotations.NotNull

class WriteActionUtils {
    companion object{
        fun runWriteAction(@NotNull action: Runnable) {
            val application = ApplicationManager.getApplication()
            if (application.isDispatchThread) {
                application.runWriteAction(action)
            } else {
                application.invokeLater { application.runWriteAction(action) }
            }
        }

        fun runReadAction(@NotNull action: Runnable) {
            val application = ApplicationManager.getApplication()
            if (application.isDispatchThread) {
                application.runReadAction (action)
            } else {
                application.invokeLater { application.runWriteAction(action) }
            }
        }
    }
}
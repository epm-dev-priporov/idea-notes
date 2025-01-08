package dev.priporov.ideanotes.toolbar

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.AnActionButton
import com.intellij.ui.AnActionButtonRunnable
import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.dialog.OkDialog
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ToolbarPopupMenuActionGroup
import dev.priporov.ideanotes.tree.common.TreePopUpMenuManager
import dev.priporov.ideanotes.tree.exporting.ExportService
import dev.priporov.ideanotes.tree.importing.ImportService
import dev.priporov.ideanotes.tree.node.FileTreeNode
import javax.swing.Icon
import javax.swing.JPanel

object NoteToolbarFactory {

    var exportIcon: Icon = IconLoader.getIcon("/icons/menu/export.png", javaClass)

    private var settingsIcon: Icon = IconLoader.getIcon("/icons/menu/settings.png", javaClass)
    private var importIcon: Icon = IconLoader.getIcon("/icons/menu/import.png", javaClass)
    private var collapseIcon: Icon = IconLoader.getIcon("/icons/collapse_expand/collapseComponent.png", javaClass)
    private var expandIcon: Icon = IconLoader.getIcon("/icons/collapse_expand/expandComponent.png", javaClass)

    fun getInstance(tree: NoteTree): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(tree)
        decorator.setAddAction(NoteToolbarAction {
            TreePopUpMenuManager.createPopUpMenu(tree, tree.locationOnScreen, ToolbarPopupMenuActionGroup(tree))
        })

        decorator.setRemoveAction(NoteToolbarAction {
            OkDialog("Delete node") {
                tree.getSelectedNodes(FileTreeNode::class.java, null).filterNotNull().forEach { tree.delete(it) }
            }.show()
        })
        decorator.addExtraAction(
            NoteToolbarActionButton({ tree.expandAll() }, "Expand All", expandIcon)
        )
        decorator.addExtraAction(
            NoteToolbarActionButton({ tree.collapseAll() }, "Collapse All", collapseIcon)
        )
        decorator.addExtraAction(
            NoteToolbarActionButton({ service<ImportService>().import(tree).invoke() }, "Import", importIcon)
        )
        decorator.addExtraAction(
            NoteToolbarActionButton({ service<ExportService>().exportNotesToFile(tree).invoke() }, "Export All", exportIcon)
        )
        decorator.addExtraAction(
                NoteToolbarActionButton({ ShowSettingsUtil.getInstance().showSettingsDialog(null, "Notes Tree");}, "Settings", settingsIcon)
        )

        return decorator.createPanel()
    }

}

class NoteToolbarActionButton(
    private val function: () -> Unit,
    name: String,
    icon: Icon
) : AnAction(name, null, icon) {
    override fun actionPerformed(e: AnActionEvent) = function.invoke()

}

class NoteToolbarAction(private val function: () -> Unit) : AnActionButtonRunnable {
    override fun run(t: AnActionButton) = function.invoke()
}
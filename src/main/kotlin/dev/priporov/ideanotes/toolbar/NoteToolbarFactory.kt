package dev.priporov.ideanotes.toolbar

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.AnActionButton
import com.intellij.ui.AnActionButtonRunnable
import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.tree.common.ToolbarPopupMenuActionGroup
import dev.priporov.ideanotes.tree.common.TreePopUpMenuManager
import dev.priporov.ideanotes.tree.exporting.ExportService
import dev.priporov.ideanotes.tree.importing.ImportService
import dev.priporov.ideanotes.tree.node.FileTreeNode
import dev.priporov.noteplugin.component.dialog.OkDialog
import javax.swing.Icon
import javax.swing.JPanel

object NoteToolbarFactory {

    private val exportService = service<ExportService>()
    private val importService = service<ImportService>()

    private var settingsIcon: Icon = IconLoader.getIcon("/icons/menu/settings.png", javaClass)

    private var importIcon: Icon = IconLoader.getIcon("/icons/menu/import.png", javaClass)

    var exportIcon: Icon = IconLoader.getIcon("/icons/menu/export.png", javaClass)

    fun getInstance(tree: NoteTree): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(tree)
        decorator.setAddAction(NoteToolbarAction {
            TreePopUpMenuManager.createPopUpMenu(tree, tree.locationOnScreen, ToolbarPopupMenuActionGroup(tree))
        })

        decorator.setRemoveAction(NoteToolbarAction {
            OkDialog("Delete node") {
                tree.getSelectedNodes(FileTreeNode::class.java, null).forEach { tree.delete(it) }
            }.show()
        })
        decorator.addExtraAction(
            NoteToolbarActionButton({ importService.import(tree).invoke() }, "Import", importIcon)
        )
        decorator.addExtraAction(
            NoteToolbarActionButton({ exportService.exportNotesToFile(tree).invoke() }, "Export All", exportIcon)
        )
//        decorator.addExtraAction(
//                NoteToolbarActionButton({}, "Settings", settingsIcon)
//        )

        return decorator.createPanel()
    }

}

class NoteToolbarActionButton(
    private val function: () -> Unit,
    name: String,
    icon: Icon
) : AnActionButton(name, null, icon) {
    override fun actionPerformed(e: AnActionEvent) = function.invoke()

    override fun updateButton(e: AnActionEvent) {
        super.updateButton(e)
    }

}

class NoteToolbarAction(private val function: () -> Unit) : AnActionButtonRunnable {
    override fun run(t: AnActionButton) = function.invoke()
}
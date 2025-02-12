package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.components.service
import dev.priporov.ideanotes.tree.BaseTree
import dev.priporov.ideanotes.tree.action.*
import dev.priporov.ideanotes.tree.handler.DragAndDropTransferHandler
import dev.priporov.ideanotes.tree.listener.TreeMouseListener
import javax.swing.DropMode

open class BaseNoteTreeFactory<T : BaseTree<*>>() {

    protected fun init(tree: BaseTree<*>) {
        tree.addMouseListener(TreeMouseListener(tree))
        tree.expandAll()
        tree.transferHandler = service<DragAndDropTransferHandler>()
        tree.isRootVisible = false
        tree.setDropMode(DropMode.ON_OR_INSERT)
        tree.dragEnabled = true

        ActionUtil.getShortcutSet("ShowPopupMenu").shortcuts.also { shortcutSet ->
            ShowTreePopUpMenuAction(tree).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), tree)
        }
        ActionUtil.getShortcutSet("\$Copy").shortcuts.also { shortcutSet ->
            CopyNodeAction(tree).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), tree)
        }
        ActionUtil.getShortcutSet("\$Cut").shortcuts.also { shortcutSet ->
            CutNodeAction(tree).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), tree)
        }
        ActionUtil.getShortcutSet("\$Paste").shortcuts.also { shortcutSet ->
            PasteNodeAction(tree).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), tree)
        }
        ActionUtil.getShortcutSet("RenameElement").shortcuts.also { shortcutSet ->
            RenameNodeAction(tree).registerCustomShortcutSet(CustomShortcutSet(*shortcutSet), tree)
        }

    }

}
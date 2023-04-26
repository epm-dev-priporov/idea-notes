package dev.priporov.ideanotes.listener

import com.intellij.openapi.keymap.KeymapUtil
import dev.priporov.ideanotes.tree.NoteTree
import dev.priporov.ideanotes.dialog.OkDialog
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class NoteKeyListener(private val tree: NoteTree) : KeyListener {

    override fun keyTyped(e: KeyEvent?) {}
    override fun keyReleased(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent) {
        when {
            isEnter(e) -> openTextEditor()
            isDelete(e) -> deleteNodeDialog()
        }
    }

    private fun isEnter(e: KeyEvent) = KeymapUtil.getKeyText(e.keyCode) == "Enter"

    private fun isDelete(e: KeyEvent) = KeymapUtil.getKeyText(e.keyCode) == "Delete"

    private fun openTextEditor() {
        tree.getSelectedNode()?.also { node -> tree.openInEditor(node) }
    }

    private fun deleteNodeDialog() {
        tree.getSelectedNode()?.also { node ->
            OkDialog("Delete node") { tree.delete(node) }.show()
        }
    }

}
package dev.priporov.ideanotes.main

import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.tree.NoteTree
import java.awt.BorderLayout
import javax.swing.JPanel

class MainNoteToolWindow(val tree: NoteTree) : JPanel() {

    init {
        val decorator = ToolbarDecorator.createDecorator(tree)
        val toolbarPanel = decorator.createPanel()

        layout = BorderLayout().apply {
            add(toolbarPanel)
            addLayoutComponent(tree, "Center")
        }
        add(toolbarPanel)
        add(tree)
    }
}
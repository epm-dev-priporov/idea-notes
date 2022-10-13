package dev.priporov.ideanotes.main

import com.intellij.openapi.components.service
import com.intellij.ui.treeStructure.Tree
import dev.priporov.ideanotes.tree.NoteTree
import java.awt.BorderLayout
import javax.swing.JPanel

class MainNoteToolWindow : JPanel() {
    private val noteTree: Tree = service<NoteTree>()

    init {
        layout = BorderLayout().apply {
            addLayoutComponent(noteTree, "Center")
        }
        add(noteTree)
    }
}
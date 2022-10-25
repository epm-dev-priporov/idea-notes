package dev.priporov.ideanotes.main

import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.SideBorder
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.TreeSpeedSearch
import dev.priporov.ideanotes.toolbar.NoteToolbarFactory
import dev.priporov.ideanotes.tree.NoteTree
import java.awt.BorderLayout
import javax.swing.JPanel

class MainNoteToolWindow(val tree: NoteTree) : JPanel() {

    init {
        val toolbarPanel = NoteToolbarFactory.getInstance(tree)
        val createScrollPane = ScrollPaneFactory.createScrollPane(
            TreeSpeedSearch(tree).component,
            SideBorder.TOP
        );

        layout = BorderLayout().apply {
            add(createScrollPane)
            addLayoutComponent(toolbarPanel, "North")
            addLayoutComponent(tree, "Center")
        }
        add(createScrollPane)
        add(toolbarPanel)
        add(tree)
    }
}
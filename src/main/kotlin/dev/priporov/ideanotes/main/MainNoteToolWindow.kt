package dev.priporov.ideanotes.main

import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.SideBorder
import com.intellij.ui.TreeUIHelper
import com.intellij.ui.components.JBScrollBar
import com.intellij.ui.components.JBScrollPane
import dev.priporov.ideanotes.toolbar.NoteToolbarFactory
import dev.priporov.ideanotes.tree.NoteTree
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.ScrollPaneConstants

class MainNoteToolWindow(val tree: NoteTree) : JPanel() {

    init {
        val toolbarPanel = NoteToolbarFactory.getInstance(tree)

        TreeUIHelper.getInstance().installTreeSpeedSearch(tree)
        val jbScrollPane = JBScrollPane()
        jbScrollPane.setViewportView(tree)
//        jbScrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS

        val createScrollPane = ScrollPaneFactory.createScrollPane(
            jbScrollPane,
            SideBorder.TOP
        )

        layout = BorderLayout().apply {
            add(createScrollPane)
            addLayoutComponent(toolbarPanel, "North")
            addLayoutComponent(tree, "Center")
        }
        add(createScrollPane)
        add(toolbarPanel)
        add(jbScrollPane)
    }
}
package dev.priporov.ideanotes.tree.panel

import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.SideBorder
import com.intellij.ui.TreeUIHelper
import com.intellij.ui.components.JBScrollPane
import dev.priporov.ideanotes.tree.BaseTree
import java.awt.BorderLayout
import javax.swing.JPanel

class TreePanel(private val tree: BaseTree<*>, private val toolbarPanel: JPanel) : JPanel() {
    init {
        TreeUIHelper.getInstance().installTreeSpeedSearch(tree)
        val jbScrollPane = JBScrollPane().apply {
            setViewportView(tree)
        }
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
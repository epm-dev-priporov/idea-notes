package dev.priporov.ideanotes.main

import com.intellij.ui.components.JBTabbedPane
import javax.swing.JPanel

class TabbedPanel(appTreePanel: JPanel, projectTreePanel: JPanel) : JBTabbedPane() {

    init {
        addTab("General", appTreePanel)
        addTab("Project", projectTreePanel)
    }

}
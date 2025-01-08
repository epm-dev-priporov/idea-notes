package dev.priporov.ideanotes.main

import com.intellij.ui.components.JBTabbedPane
import javax.swing.JPanel

class TabbedPanel(globalTreePanel: JPanel) : JBTabbedPane() {

    init {
        addTab("Global notes", globalTreePanel)
    }

}
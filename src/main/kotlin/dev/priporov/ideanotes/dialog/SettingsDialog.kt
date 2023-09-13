package dev.priporov.ideanotes.dialog

import com.intellij.diff.comparison.expand
import com.intellij.openapi.ui.DialogWrapper
import dev.priporov.ideanotes.util.FileNodeUtils
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFormattedTextField
import javax.swing.JPanel

class SettingsDialog(
    var panel: JPanel = JPanel(),
    var button: JButton = JButton(),
    var formattedTextField: JFormattedTextField = JFormattedTextField()
) : DialogWrapper(true) {
    init {
        formattedTextField.apply {
            text = FileNodeUtils.baseDir.path
        }
        title = "Settings"
        init()
    }

    override fun createCenterPanel(): JComponent? {
        return panel
    }

}
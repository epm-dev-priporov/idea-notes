package dev.priporov.ideanotes.dialog

import dev.priporov.ideanotes.util.FileNodeUtils
import javax.swing.JButton
import javax.swing.JFormattedTextField
import javax.swing.JPanel

class SettingsDialog(
    var root: JPanel = JPanel(),
    var button: JButton = JButton(),
    var formattedTextField: JFormattedTextField = JFormattedTextField()
) {
    init {
        formattedTextField.apply {
            text = FileNodeUtils.baseDir.path
        }

    }

}
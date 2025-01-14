package dev.priporov.ideanotes.tree.dialog

import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class OkDialog(
    title: String,
    private val function: () -> Unit
) : DialogWrapper(true) {

    private val borderLayout = BorderLayout().apply {
    }
    private val panel = JPanel().apply {
        layout = borderLayout
    }

    init {
        init()
        this.title = title
    }

    override fun createCenterPanel(): JComponent = panel
    override fun doOKAction() {
        function.invoke()
        super.doOKAction()
    }

}
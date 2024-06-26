package dev.priporov.ideanotes.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.EditorTextField
import org.apache.commons.lang3.StringUtils
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class EditDialog(
    title: String,
    stringForRename: String? = null,
    private val function: (String) -> Unit
) : DialogWrapper(true) {
    private val edit = EditorTextField()
    private val borderLayout = BorderLayout().apply {
        addLayoutComponent(edit, BorderLayout.NORTH)
    }
    private val panel = JPanel().apply {
        add(edit)
        layout = borderLayout
    }

    init {
        init()
        this.title = title
        if (StringUtils.isNotBlank(stringForRename)) {
            edit.text = stringForRename!!
        }
    }

    override fun createCenterPanel(): JComponent = panel
    override fun getPreferredFocusedComponent(): JComponent = edit
    override fun doOKAction() {
        if (edit.text.isNotBlank()) {
            function.invoke(edit.text)
            super.doOKAction()
        }
    }

}
package dev.priporov.ideanotes.configuration

import com.intellij.openapi.options.SearchableConfigurable
import dev.priporov.ideanotes.dialog.SettingsDialog
import javax.swing.JComponent

class NotesTreeConfigurable : SearchableConfigurable {

    private val gui: SettingsDialog by lazy {
        SettingsDialog()
    }

    override fun createComponent(): JComponent = gui.root

    override fun isModified() = false

    override fun apply() {

    }

    override fun getDisplayName() = "Notes Tree"

    override fun getId() = "dev.priporov.ideanotes.configuration.NotesTreeConfigurable"
}
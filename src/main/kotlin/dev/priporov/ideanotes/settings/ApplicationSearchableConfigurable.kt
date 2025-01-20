package dev.priporov.ideanotes.settings

import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent
import javax.swing.JPanel

class ApplicationSearchableConfigurable: SearchableConfigurable {

    override fun createComponent(): JComponent {
        return JPanel()
    }

    override fun isModified(): Boolean {
        return true
    }

    override fun apply() {
        TODO("Not yet implemented")
    }

    override fun getDisplayName() = "Notes Tree"

    override fun getId() = "dev.priporov.ideanotes.settings.ApplicationSearchableConfigurable"

}
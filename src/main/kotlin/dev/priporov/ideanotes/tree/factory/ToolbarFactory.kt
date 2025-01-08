package dev.priporov.ideanotes.tree.factory

import com.intellij.openapi.components.Service
import com.intellij.ui.ToolbarDecorator
import dev.priporov.ideanotes.tree.NoteTree
import javax.swing.JPanel

@Service
class ToolbarFactory {

    fun getInstance(tree: NoteTree): JPanel {
        val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(tree)

        return decorator.createPanel()
    }

}
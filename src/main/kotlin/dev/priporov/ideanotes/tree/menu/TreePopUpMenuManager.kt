package dev.priporov.ideanotes.tree.menu

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopupStep
import com.intellij.ui.awt.RelativePoint
import dev.priporov.ideanotes.tree.BaseTree
import java.awt.Point

class TreePopUpMenuManager {
    companion object {
        fun createPopUpMenu(tree: BaseTree<*>, point: Point, actionGroup: DefaultActionGroup) {

            val factory = JBPopupFactory.getInstance()

            val popupStep: ListPopupStep<Any> = factory.createActionsStep(
                actionGroup,
                DataManager.getInstance().getDataContext(tree),
                ActionPlaces.POPUP,
                false,
                true,
                "",
                tree,
                true,
                0,
                true
            )

            factory.createListPopup(popupStep).show(RelativePoint(point))
        }
    }
}

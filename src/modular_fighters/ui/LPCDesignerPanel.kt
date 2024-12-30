package modular_fighters.ui

import com.fs.starfarer.api.ui.CustomPanelAPI
import lunalib.lunaExtensions.addLunaSpriteElement
import lunalib.lunaUI.elements.LunaSpriteElement
import modular_fighters.misc.clearChildren

class LPCDesignerPanel(var parent: CustomPanelAPI) {


    fun init() {
        recreatePanel()
    }

    fun recreatePanel() {
        parent.clearChildren()

        var width = 800f
        var height = 500f

        var backgroundPanel = parent.createCustomPanel(width, height, null)
        parent.addComponent(backgroundPanel)
        var backgroundElement = backgroundPanel.createUIElement(width, height, false)
        backgroundPanel.addUIElement(backgroundElement)


        backgroundElement.addLunaSpriteElement("graphics/ui/LPCDesignerBackground.png", LunaSpriteElement.ScalingTypes.STRETCH_SPRITE, 800f, 500f).apply {
            enableTransparency = true
            position.inTL(0f, 0f)
            getSprite().alphaMult = 0.8f
        }

        var panel = parent.createCustomPanel(width, height, null)
        parent.addComponent(panel)


    }

}
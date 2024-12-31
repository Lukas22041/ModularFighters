package modular_fighters.ui

import com.fs.starfarer.api.ui.CustomPanelAPI
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaExtensions.addLunaSpriteElement
import lunalib.lunaUI.elements.LunaSpriteElement
import modular_fighters.ModularFighterUtils
import modular_fighters.misc.clearChildren
import modular_fighters.ui.elements.FighterListElement
import java.awt.Color

class LPCDesignerPanel(var parent: CustomPanelAPI) {

    companion object {
        var lastScrollerY = 0f
    }

    var data = ModularFighterUtils.getData()

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

        /*var panel = parent.createCustomPanel(width, height, null)
        parent.addComponent(panel)*/

        recreateListPanel()

    }

    fun recreateListPanel() {

        var dataMap = data.fighterData

        var width = 225f
        var height = 500f

        var listPanel = parent.createCustomPanel(width, height, null)
        parent.addComponent(listPanel)

        var backgroundElement = listPanel.createUIElement(width, height, false)
        listPanel.addUIElement(backgroundElement)
        var listBackground = backgroundElement.addLunaElement(width, height).apply {
            enableTransparency = true
            renderBorder = false
            backgroundColor = Color(20, 20, 20, 75)
            position.inTL(0f, 0f)
        }


        var listElement = listPanel.createUIElement(width, height, true)

        var first = true
        for ((spec, data) in dataMap) {
            var element = FighterListElement(data, listElement, width - 12f, 50f)

            if (first) {
                first = false
                element.position.inTL(5f, 10f)
            }
        }

        listElement.addSpacer(20f)

        listPanel.addUIElement(listElement)
        listElement.externalScroller.yOffset = lastScrollerY
        listBackground.advance { lastScrollerY = listElement.externalScroller.yOffset }
    }

}
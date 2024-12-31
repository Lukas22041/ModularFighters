package modular_fighters.ui

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaExtensions.addLunaSpriteElement
import lunalib.lunaUI.elements.LunaSpriteElement
import modular_fighters.ModularFighterUtils
import modular_fighters.components.ModularFighterData
import modular_fighters.misc.addTooltip
import modular_fighters.misc.clearChildren
import modular_fighters.ui.elements.LPCSlotElement
import modular_fighters.ui.elements.ChassisDisplayElement
import modular_fighters.ui.elements.FighterListElement
import org.lwjgl.util.vector.Vector2f
import java.awt.Color

class LPCDesignerPanel(var parent: CustomPanelAPI) {

    companion object {
        var lastScrollerY = 0f
        var selectedFighter: ModularFighterData =  ModularFighterUtils.getData().fighterData.values.first()
    }

    var modData = ModularFighterUtils.getData()

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
        recreateRefitPanel()

    }

    fun recreateListPanel() {

        var dataMap = modData.fighterData

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
        var index = 0
        for ((spec, data) in dataMap) {
            if (index >= modData.visibleEntries) break

            var element = FighterListElement(data, listElement, width - 12f, 50f)

            if (first) {
                first = false
                element.position.inTL(5f, 10f)
            }

            element.advance {
                var alpha = 0f
                if (element.isHovering) alpha += 0.3f
                if (element.data == selectedFighter) alpha += 0.4f
                element.backgroundAlpha = alpha
            }

            element.onClick {
                selectedFighter = element.data
                recreatePanel()
            }

            index++
        }

        listElement.addSpacer(10f)

        if (modData.visibleEntries < modData.fighterData.keys.size) {
            var addButton = listElement.addLunaElement(width - 12f, 30f).apply {
                enableTransparency = true
                borderAlpha = 0.15f
                backgroundAlpha = 0.3f

                innerElement.setParaFont("graphics/fonts/victor14.fnt")

                addText("Add a new design", Misc.getBasePlayerColor())
                centerText()
                onHoverEnter {
                    playScrollSound()
                    backgroundAlpha = 0.5f
                }
                onHoverExit {
                    backgroundAlpha = 0.3f
                }

                onClick {
                    playClickSound()
                    modData.visibleEntries += 1
                    recreatePanel()
                }
            }

            var count = modData.fighterData.keys.size
            listElement.addTooltip(addButton.elementPanel, TooltipMakerAPI.TooltipLocation.BELOW, 300f) {
                tooltip -> tooltip.addPara("Create another slot for a new LPC design. You can have at most $count designs per save.", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "$count")
            }
        }



        listElement.addSpacer(20f)

        listPanel.addUIElement(listElement)
        listElement.externalScroller.yOffset = lastScrollerY
        listBackground.advance { lastScrollerY = listElement.externalScroller.yOffset }
    }

    fun recreateRefitPanel() {

        var data = selectedFighter

        var listWidth = 225f

        var width = 800f - listWidth
        var height = 500f

        var centerX = width / 2f
        var centerY = height / 2f

        var scale = 2f

        var panel = parent.createCustomPanel(width, height, null)
        parent.addComponent(panel)
        panel.position.inTL(listWidth, 0f)
        var element = panel.createUIElement(width, height, false)
        panel.addUIElement(element)

        var chassisChooser = LPCSlotElement(280f, Vector2f(panel.position.x + centerX, panel.position.y + centerY), element, 84f, 84f)
        chassisChooser.position.inTL(140f, 100f)

        var engineChooser = LPCSlotElement(170f, Vector2f(panel.position.x + centerX, panel.position.y + centerY), element, 92f, 42f)
        engineChooser.position.inTL(350f, 300f)

        var chassis = data.chassis
        var sprite = Global.getSettings().getSprite(chassis.getSpriteName())
        sprite.setSize(sprite.width * scale, sprite.height * scale)

        var chassisElement = ChassisDisplayElement(chassis, sprite, element, sprite.width, sprite.height)
        chassisElement.position.inTL(centerX - sprite.width / 2, centerY - sprite.height / 2)

        //element.addPara("Test", 0f).position.inTL(centerX, centerY)
    }

}
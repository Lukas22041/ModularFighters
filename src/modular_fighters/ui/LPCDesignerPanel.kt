package modular_fighters.ui

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import com.fs.starfarer.loading.specs.BaseWeaponSpec
import com.fs.starfarer.ui.impl.StandardTooltipV2
import com.fs.starfarer.ui.impl.StandardTooltipV2Expandable
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaExtensions.addLunaSpriteElement
import lunalib.lunaUI.elements.LunaSpriteElement
import modular_fighters.ModularFighterUtils
import modular_fighters.components.ModularFighterData
import modular_fighters.misc.ReflectionUtils
import modular_fighters.misc.addTooltip
import modular_fighters.misc.clearChildren
import modular_fighters.ui.elements.*
import org.lazywizard.lazylib.combat.entities.SimpleEntity
import org.lazywizard.lazylib.ext.plus
import org.lazywizard.lazylib.ext.rotate
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
        var center = Vector2f(centerX, centerY)

        var scale = 2f

        var panel = parent.createCustomPanel(width, height, null)
        parent.addComponent(panel)
        panel.position.inTL(listWidth, 0f)
        var element = panel.createUIElement(width, height, false)
        panel.addUIElement(element)



        var builtins = data.getChassis().getChassisSpec().builtInWeapons.toList()
        var placeholderEntity = SimpleEntity(Vector2f(0f, 0f))




        //Chassis Slot
        var chassisChooser = LPCSlotElement(280f, Vector2f(panel.position.x + centerX, panel.position.y + centerY), element, 84f, 84f)
        chassisChooser.position.inTL(50f, 50f)

        var chassis = data.getChassis()
        var chassisSpec = chassis.getChassisSpec()
        /*  var chassisChooserSprite = Global.getSettings().getSprite(chassis.getSpriteName())
          chassisChooserSprite.setSize(chassisChooserSprite.width * scale, chassisChooserSprite.height * scale)*/

        //Holographic Chassis Preview
        var chassisSlotDisplayer = ChassisSelectorDisplayElement(chassis, element, 84f, 84f)
        chassisSlotDisplayer.position.inTL(50f, 50f)

        //Engine Slot Elements
        var enginePosition = Vector2f(center)
        var engineBoxPosition = Vector2f(350f, 300f)

        var engineSlotPosData = builtins.find { it.second == "modular_fighters_engine_pos" }
        var engineSlotBoxData = builtins.find { it.second == "modular_fighters_engine_box" }
        var engineAngle = 170f

        if (engineSlotPosData != null) {
            var engineSlot = data.getChassis().getChassisSpec().getWeaponSlot(engineSlotPosData.first)
            enginePosition = engineSlot.computePosition(placeholderEntity).rotate(90f).scale(scale) as Vector2f
            enginePosition = center.plus(enginePosition)
        }

        if (engineSlotBoxData != null) {
            var engineSlot = data.getChassis().getChassisSpec().getWeaponSlot(engineSlotBoxData.first)
            engineBoxPosition = engineSlot.computePosition(placeholderEntity).rotate(90f).scale(scale) as Vector2f
            engineBoxPosition.y = - engineBoxPosition.y //Need to invert Y
            engineBoxPosition = center.plus(engineBoxPosition)
            engineAngle = engineSlot.angle + 90f
        }


        var engineChooser = LPCSlotElement(engineAngle, Vector2f(panel.position.x + enginePosition.x, panel.position.y + enginePosition.y), element, 92f, 42f)
        engineChooser.position.inTL(engineBoxPosition.x, engineBoxPosition.y)



        //Subsystem Slot Elements
        var subsystemSlotPositionsData = builtins.filter { it.second == "modular_fighters_subsystem_pos" }
        var subsystemSlotBoxesData = builtins.filter { it.second == "modular_fighters_subsystem_box" }

        for (slotIndex in subsystemSlotPositionsData.indices) {
            var subsystemSlotPosData = subsystemSlotPositionsData[slotIndex]
            var subsystemSlotBoxData = subsystemSlotBoxesData[slotIndex]

            var subsystemPosition = Vector2f(center)
            var subsystemBoxPosition = Vector2f(350f, 300f)
            var subsystemAngle = 0f


            //Position on Ship
            var subsystemSlotPos = data.getChassis().getChassisSpec().getWeaponSlot(subsystemSlotPosData.first)
            subsystemPosition = subsystemSlotPos.computePosition(placeholderEntity).rotate(90f).scale(scale) as Vector2f
            subsystemPosition = center.plus(subsystemPosition)

            //Position of the box
            var subsystemBoxPos = data.getChassis().getChassisSpec().getWeaponSlot(subsystemSlotBoxData.first)
            subsystemBoxPosition = subsystemBoxPos.computePosition(placeholderEntity).rotate(90f).scale(scale) as Vector2f
            subsystemBoxPosition.y = - subsystemBoxPosition.y //Need to invert Y
            subsystemBoxPosition = center.plus(subsystemBoxPosition)
            subsystemAngle = subsystemBoxPos.angle + 90f

            var subsystemChooser = LPCSlotElement(subsystemAngle, Vector2f(panel.position.x + subsystemPosition.x, panel.position.y + subsystemPosition.y), element, 42f, 42f)
            subsystemChooser.position.inTL(subsystemBoxPosition.x, subsystemBoxPosition.y)
        }


        //Chassis Renderer
        var chassisElementSprite = Global.getSettings().getSprite(chassis.getSpriteName())
        chassisElementSprite.setSize(chassisElementSprite.width * scale, chassisElementSprite.height * scale)

        var chassisElement = ChassisDisplayElement(chassis, chassisElementSprite, element, chassisElementSprite.width, chassisElementSprite.height)
        chassisElement.position.inTL(centerX - chassisElementSprite.width / 2, centerY - chassisElementSprite.height / 2)

        //element.addPara("Test", 0f).position.inTL(centerX, centerY)

        var spriteSpec = ReflectionUtils.invoke("getSpriteSpec", chassis.getChassisSpec())


        //Mounts

        var mounts = chassis.getChassisSpec().allWeaponSlotsCopy.filter { !it.isDecorative }
        for (mount in mounts) {
            var spec = Global.getSettings().getWeaponSpec("minipulser")

            var mountSize = 30f

            var mountElement = WeaponMountElement(mount, null, scale, element, mountSize, mountSize)

            var mountPosition = mount.computePosition(placeholderEntity).rotate(90f).scale(scale) as Vector2f
            mountPosition.y =- mountPosition.y //Need to invert Y



            mountPosition = center.plus(mountPosition)


            mountElement.position.inTL(mountPosition.x - mountSize / 2, mountPosition.y - mountSize / 2)

            var memberStats = Global.getSector().playerFleet.fleetData.membersListCopy.first().stats
            var weaponDesc = ReflectionUtils.invokeStatic(3, "createWeaponTooltip", StandardTooltipV2::class.java, spec, Global.getSector().playerPerson.stats, memberStats) as StandardTooltipV2Expandable
            ReflectionUtils.invokeStatic(3, "addTooltipLeft", StandardTooltipV2Expandable::class.java, mountElement.elementPanel, weaponDesc, 150f)
        }


        //Need a seperate element, they are also not included in the main list of mounts
        //Decorative Mounts
        var decorativeMounts = chassisSpec.builtInWeapons.map { chassisSpec.getWeaponSlot(it.key) }.filter { it.isDecorative }
        decorativeMounts = decorativeMounts
            .filter { it.id != "modular_fighters_engine_box" && it.id != "modular_fighters_engine_pos" && it.id != "modular_fighters_subsystem_box" && it.id != "modular_fighters_subsystem_pos" }

    }

}
package modular_fighters.ui

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.fleet.FleetGoal
import com.fs.starfarer.api.fleet.FleetMemberType
import com.fs.starfarer.api.impl.campaign.ids.Factions
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.loading.WeaponSpecAPI
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import com.fs.starfarer.campaign.CampaignEngine
import com.fs.starfarer.combat.CombatFleetManager
import com.fs.starfarer.ui.impl.StandardTooltipV2
import com.fs.starfarer.ui.impl.StandardTooltipV2Expandable
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaExtensions.addLunaSpriteElement
import lunalib.lunaExtensions.addLunaTextfield
import lunalib.lunaUI.elements.LunaElement
import lunalib.lunaUI.elements.LunaSpriteElement
import modular_fighters.ModularFighterUtils
import modular_fighters.components.BaseFighterComponent
import modular_fighters.components.ComponentPluginLoader
import modular_fighters.components.ModularFighterData
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.components.engines.BaseFighterEngine
import modular_fighters.components.subsystems.BaseFighterSubsystem
import modular_fighters.misc.*
import modular_fighters.modifier.FighterStatsObject
import modular_fighters.ui.elements.*
import org.lazywizard.lazylib.combat.entities.SimpleEntity
import org.lazywizard.lazylib.ext.plus
import org.lazywizard.lazylib.ext.rotate
import org.lwjgl.util.vector.Vector2f
import org.magiclib.kotlin.getStorageCargo
import modular_fighters.ui.tooltips.ComponentTooltipCreator
import org.lazywizard.lazylib.ext.campaign.addShip
import org.lwjgl.input.Keyboard
import java.awt.Color

class LPCDesignerPanel(var refitButton: ModularFightersRefitButton, var parent: CustomPanelAPI, var market: MarketAPI?) {

    companion object {
        var lastScrollerY = 0f
        var selectedFighter: ModularFighterData =  ModularFighterUtils.getData().fighterData.values.first()
    }

    var modData = ModularFighterUtils.getData()
    var selectedListElement: FighterListElement? = null
    var selectedMount: WeaponMountElement? = null


    fun init() {
        recreatePanel()
    }

    fun recreatePanel() {
        refitButton.refreshVariant() //Required as otherwise it crashes if a demo is built while a fighter is slotted in.

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
            if (selectedFighter == data) selectedListElement = element

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
                selectedListElement = element
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
                    playSound("ui_button_mouseover", 1f, 1f)
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

        element.addTooltipTo(ComponentTooltipCreator(chassis), chassisSlotDisplayer.elementPanel, TooltipMakerAPI.TooltipLocation.LEFT)

        chassisSlotDisplayer.onClick {
            chassisSlotDisplayer.playClickSound()
            openComponentPicker(BaseFighterComponent.ComponentType.CHASSIS, panel, element, chassisSlotDisplayer, 0)
        }

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

        var fittedWeapons = data.fittedWeapons
        var mounts = chassis.getChassisSpec().allWeaponSlotsCopy.filter { !it.isDecorative }
        for (mount in mounts) {

            var spec: WeaponSpecAPI? = null
            var weapon = fittedWeapons.get(mount.id)
            if (weapon != null) spec = Global.getSettings().getWeaponSpec(weapon)
            //var spec = Global.getSettings().getWeaponSpec("minipulser")

            var mountSize = 30f

            var mountElement = WeaponMountElement(this, mount, spec, scale, element, mountSize, mountSize)

            var mountPosition = mount.computePosition(placeholderEntity).rotate(90f).scale(scale) as Vector2f
            mountPosition.y =- mountPosition.y //Need to invert Y



            mountPosition = center.plus(mountPosition)


            mountElement.position.inTL(mountPosition.x - mountSize / 2, mountPosition.y - mountSize / 2)


            if (spec != null) {
                var memberStats = Global.getSector().playerFleet.fleetData.membersListCopy.first().stats
                var weaponDesc = ReflectionUtils.invokeStatic(3, "createWeaponTooltip", StandardTooltipV2::class.java, spec, Global.getSector().playerPerson.stats, memberStats) as StandardTooltipV2Expandable
                ReflectionUtils.invokeStatic(3, "addTooltipLeft", StandardTooltipV2Expandable::class.java, mountElement.elementPanel, weaponDesc, 150f)
                weaponDesc.setBeforeShowing {
                    ReflectionUtils.invokeByNameAlone("setTooltipPositionRelativeToAnchor", mountElement.elementPanel, -weaponDesc.width + 100, (height - weaponDesc.getHeight()) / 2, element)
                }
            }

            mountElement.onClick {
                if (it.isLMBEvent) {
                    openWeaponPicker(panel, element, mountElement)
                }
                if (it.isRMBEvent) {
                    data.fittedWeapons.remove(mount.id)
                    mountElement.playSound("ui_refit_slot_cleared_small", 1f, 1f)
                    recreatePanel()
                }
            }

        }


        //Need a seperate element, they are also not included in the main list of mounts
        //Decorative Mounts
        var decorativeMounts = chassisSpec.builtInWeapons.map { chassisSpec.getWeaponSlot(it.key) }.filter { it.isDecorative }
        decorativeMounts = decorativeMounts
            .filter { it.id != "modular_fighters_engine_box" && it.id != "modular_fighters_engine_pos" && it.id != "modular_fighters_subsystem_box" && it.id != "modular_fighters_subsystem_pos" }





        var finishBWidth = 150f
        var finishBHeight = 30f
        //Complete Design/Craft Button
        var finishButton = element.addLunaElement(finishBWidth, finishBHeight).apply {
            enableTransparency = true
            borderAlpha = 0.7f
            backgroundAlpha = 0.25f

            onClick {
                playClickSound()

                data.applyDataToSpecs()
                Global.getSector().playerFleet.cargo.addFighters(data.fighterWingSpecId, 1)
                Global.getSector().campaignUI.messageDisplay.addMessage("Crafted 1 ${data.name}-Wing LPC")

                recreatePanel()
            }

            onHoverEnter {
                playSound("ui_button_mouseover", 1f, 1f)
                backgroundAlpha = 0.6f
            }
            onHoverExit {
                backgroundAlpha = 0.25f
            }

            innerElement.setParaFont("graphics/fonts/victor14.fnt")
            var textPara = innerElement.addPara("Complete Design", 0f, Misc.getBasePlayerColor(), Misc.getBasePlayerColor())
            textPara!!.position.inTL(finishBWidth / 2 - textPara!!.computeTextWidth(textPara!!.text) / 2 - 1, finishBHeight / 2 - textPara!!.computeTextHeight(textPara!!.text) / 2)

            position.inTL(width - finishBWidth - 10f, height - finishBHeight - 10f)
        }

        var simBWidth = 120f
        var simBHeigh = 30f
        //Complete Design/Craft Button
        var simButton = element.addLunaElement(simBWidth, simBHeigh).apply {
            enableTransparency = true
            borderAlpha = 0.7f
            backgroundAlpha = 0.25f

            if (market == null) {
                backgroundColor = Misc.getDarkPlayerColor().darker().darker().darker()
                backgroundAlpha = 0.5f
            }

            onClick {
                if (market == null) {
                    playSound("ui_char_can_not_increase_skill_or_aptitude", 1f, 1f)
                    return@onClick
                }

                playClickSound()

                data.applyDataToSpecs()

                recreatePanel()

                startSimulation(data.fighterWingSpecId)
            }

            onHoverEnter {
                playSound("ui_button_mouseover", 1f, 1f)
                backgroundAlpha = 0.6f
                if (market == null) {
                    backgroundAlpha = 0.7f
                }
            }
            onHoverExit {
                backgroundAlpha = 0.25f
                if (market == null) {
                    backgroundAlpha = 0.5f
                }
            }

            innerElement.setParaFont("graphics/fonts/victor14.fnt")
            var textPara = innerElement.addPara("Simulation", 0f, Misc.getBasePlayerColor(), Misc.getBasePlayerColor())
            textPara!!.position.inTL(simBWidth / 2 - textPara!!.computeTextWidth(textPara!!.text) / 2 - 1, simBHeigh / 2 - textPara!!.computeTextHeight(textPara!!.text) / 2)

            position.leftOfTop(finishButton.elementPanel, 5f)
        }

        element.addTooltip(simButton.elementPanel, TooltipMakerAPI.TooltipLocation.ABOVE, 350f) { tooltip ->

            tooltip.addPara("Opens up a simulation with an invincible Drover-class, carrying two of the currently edited fighter wings. The opponent is an Enforcer-class destroyer. Your ship is ignored by the enemy AI.")

            if (market == null) {
                tooltip.addSpacer(10f)
                tooltip.addNegativePara("Can only be opened while docked to a market.")
            }
        }

        //Name Field

        var nameBWidth = 170f
        var nameBHeight = 25f
        //Complete Design/Craft Button
        var nameField = element.addLunaTextfield(data.name, false, nameBWidth, nameBHeight).apply {
            enableTransparency = true
            borderAlpha = 0.7f
            backgroundAlpha = 0.25f

            onClick {
                playClickSound()
            }

            onHoverEnter {
                backgroundAlpha = 0.6f
            }
            onHoverExit {
                backgroundAlpha = 0.25f
            }

            this.changeFont("graphics/fonts/victor14.fnt")
           /* var textPara = innerElement.addPara("Complete Design", 0f, Misc.getBasePlayerColor(), Misc.getBasePlayerColor())
            textPara!!.position.inTL(nameBWidth / 2 - textPara!!.computeTextWidth(textPara!!.text) / 2 - 1, nameBHeight / 2 - textPara!!.computeTextHeight(textPara!!.text) / 2)*/

            position.aboveRight(finishButton.elementPanel, 5f)

            advance {
                if (data.name != this.getText()) {
                    data.name = this.getText()
                    selectedListElement?.namePara?.text = data.name
                }
            }

        }



        //Preview Button

        var previewButtonWidth = 25f
        var previewButtonHeight = 25f
        //Complete Design/Craft Button
        var previewButton = element.addLunaElement(previewButtonWidth, previewButtonHeight).apply {
            enableTransparency = true
            borderAlpha = 0.7f
            backgroundAlpha = 0.25f

            onClick {
                playClickSound()
            }

            onHoverEnter {
                playSound("ui_button_mouseover", 1f, 1f)
                backgroundAlpha = 0.6f
            }
            onHoverExit {
                backgroundAlpha = 0.25f
            }

            innerElement.setParaFont("graphics/fonts/victor14.fnt")
            var textPara = innerElement.addPara("*", 0f, Misc.getBasePlayerColor(), Misc.getBasePlayerColor())
            textPara!!.position.inTL(previewButtonWidth / 2 - textPara!!.computeTextWidth(textPara!!.text) / 2, previewButtonHeight / 2 - textPara!!.computeTextHeight(textPara!!.text) / 2)

            position.leftOfTop(nameField.elementPanel, 5f)
        }

        var wingMember = Global.getFactory().createFleetMember(FleetMemberType.FIGHTER_WING, data.fighterWingSpecId)
        var memberStats = Global.getSector().playerFleet.fleetData.membersListCopy.first().stats
        var weaponDesc = ReflectionUtils.invokeStatic(2, "createFleetMemberTooltipPreDeploy", StandardTooltipV2::class.java, wingMember, Global.getSector().playerPerson.stats) as StandardTooltipV2
        ReflectionUtils.invokeStatic(3, "addTooltipLeft", StandardTooltipV2Expandable::class.java, previewButton.elementPanel, weaponDesc, 150f)

        previewButton.onHoverEnter {
            data.applyDataToSpecs() //Update before each hover.
            ReflectionUtils.set("index", weaponDesc, 1)
        }



        //Help Button
        var helpButtonWidth = 25f
        var helpButtonHeight = 25f
        //Complete Design/Craft Button
        var helpButton = element.addLunaElement(helpButtonWidth, helpButtonHeight).apply {
            enableTransparency = true
            borderAlpha = 0.7f
            backgroundAlpha = 0.25f

            onClick {
                playClickSound()
            }

            onHoverEnter {
                playSound("ui_button_mouseover", 1f, 1f)
                backgroundAlpha = 0.6f
            }
            onHoverExit {
                backgroundAlpha = 0.25f
            }

            innerElement.setParaFont("graphics/fonts/victor14.fnt")
            var textPara = innerElement.addPara("?", 0f, Misc.getBasePlayerColor(), Misc.getBasePlayerColor())
            textPara!!.position.inTL(helpButtonWidth / 2 - textPara!!.computeTextWidth(textPara!!.text) / 2 - 1, helpButtonHeight / 2 - textPara!!.computeTextHeight(textPara!!.text) / 2)

            position.leftOfTop(previewButton.elementPanel, 5f)
        }



    }

    fun openWeaponPicker(panel: CustomPanelAPI, element: TooltipMakerAPI, mountElement: WeaponMountElement) {

        var data = selectedFighter

        selectedMount = mountElement

        var cargo = Global.getSector().playerFleet.cargo.createCopy()
        if (market != null) {
            var storage = market?.getStorageCargo()
            if (storage != null) {
                cargo.addAll(storage.createCopy())
            }
        }

        if (Global.getSettings().isDevMode) {
            cargo.clear()
            for (weapon in Global.getSettings().allWeaponSpecs) {
                if (weapon.mountType == WeaponAPI.WeaponType.DECORATIVE) continue
                if (weapon.mountType == WeaponAPI.WeaponType.BUILT_IN) continue
                if (weapon.mountType == WeaponAPI.WeaponType.SYSTEM) continue
                if (weapon.mountType == WeaponAPI.WeaponType.STATION_MODULE) continue
                cargo.addWeapons(weapon.weaponId, 100)
            }
        }


        var mount = mountElement.mount
        cargo.sort()

        var weaponsInCargo = cargo.weapons
        var weapons: HashMap<WeaponSpecAPI, Int> = HashMap<WeaponSpecAPI, Int>()

        for (wep in weaponsInCargo) {
            var spec = Global.getSettings().getWeaponSpec(wep.item)
            if (spec.size != mount.slotSize) continue

            var add = false
            if (mount.weaponType == WeaponAPI.WeaponType.UNIVERSAL) add = true
            else if (mount.weaponType == WeaponAPI.WeaponType.BALLISTIC && (spec.mountType == WeaponAPI.WeaponType.BALLISTIC || spec.mountType == WeaponAPI.WeaponType.HYBRID || spec.mountType == WeaponAPI.WeaponType.COMPOSITE)) add = true
            else if (mount.weaponType == WeaponAPI.WeaponType.ENERGY && (spec.mountType == WeaponAPI.WeaponType.ENERGY || spec.mountType == WeaponAPI.WeaponType.SYNERGY || spec.mountType == WeaponAPI.WeaponType.HYBRID)) add = true
            else if (mount.weaponType == WeaponAPI.WeaponType.MISSILE && (spec.mountType == WeaponAPI.WeaponType.MISSILE || spec.mountType == WeaponAPI.WeaponType.COMPOSITE || spec.mountType == WeaponAPI.WeaponType.SYNERGY)) add = true

            else if (mount.weaponType == WeaponAPI.WeaponType.HYBRID && (spec.mountType == WeaponAPI.WeaponType.HYBRID || spec.mountType == WeaponAPI.WeaponType.BALLISTIC || spec.mountType == WeaponAPI.WeaponType.ENERGY)) add = true
            else if (mount.weaponType == WeaponAPI.WeaponType.COMPOSITE && (spec.mountType == WeaponAPI.WeaponType.COMPOSITE || spec.mountType == WeaponAPI.WeaponType.BALLISTIC || spec.mountType == WeaponAPI.WeaponType.MISSILE)) add = true
            else if (mount.weaponType == WeaponAPI.WeaponType.SYNERGY && (spec.mountType == WeaponAPI.WeaponType.SYNERGY || spec.mountType == WeaponAPI.WeaponType.ENERGY || spec.mountType == WeaponAPI.WeaponType.MISSILE)) add = true

            if (add) {
                weapons.put(spec, wep.count)
            }
        }

        var weaponsList = weapons.toList().sortedByDescending { it.first.getOrdnancePointCost(Global.getFactory().createPerson().stats) }.toMutableList()

        var pWidth = 350f
        var pHeight = 450f

        var pickerElement = ComponentPickerBackgroundElement(this, element, pWidth, pHeight).apply {
            enableTransparency = true
            backgroundColor = Color(0, 0, 0)
            borderAlpha = 1f
            position.rightOfMid(mountElement.elementPanel, 35f)
        }

        /*pickerElement.onClickOutside {
            it.consume()
            selectedMount = null
            recreatePanel()
        }*/

        var pickerList = pickerElement.elementPanel.createUIElement(pWidth, pHeight, true)
        pickerList.position.inTL(0f, 0f)

        if (weapons.isEmpty()) {
            var para = pickerList.addTitle("No suitable weapons in storage", Misc.getBasePlayerColor())
            para.position.inTL(pWidth / 2f - para.computeTextWidth(para.text) / 2, pHeight / 2 - 20f)
        }

        var isDevmode = Global.getSettings().isDevMode

        if (isDevmode) pickerList.addSectionHeading("Devmode List", Alignment.MID, 0f).apply {
            position.setSize(position.width+ 3f, position.height)
        }

        var first = true
        for ((weapon, quantity) in weaponsList) {
            var weaponListElement = WeaponListElement(mountElement, weapon, quantity, pickerList, pWidth - 5, 70f).apply {
                enableTransparency = true
                backgroundAlpha = 0.0f
                backgroundColor = Misc.getDarkPlayerColor()
                renderBorder = false

                onHoverEnter {
                    playSound("ui_button_mouseover", 1f, 1f)
                }

            }
            if (first) {
                first = false
                weaponListElement.position.inTL(5f, 3f)
                if (isDevmode) weaponListElement.position.inTL(5f, 23f)
            }
            pickerList.addSpacer(3f)
            if (isDevmode) pickerList.addSpacer(3f)


            var memberStats = Global.getSector().playerFleet.fleetData.membersListCopy.first().stats
            var weaponDesc = ReflectionUtils.invokeStatic(3, "createWeaponTooltip", StandardTooltipV2::class.java, weapon, Global.getSector().playerPerson.stats, memberStats) as StandardTooltipV2Expandable
            ReflectionUtils.invokeStatic(3, "addTooltipLeft", StandardTooltipV2Expandable::class.java, weaponListElement.elementPanel, weaponDesc, 50f)
            weaponDesc.setBeforeShowing {
                ReflectionUtils.invokeByNameAlone("setTooltipPositionRelativeToAnchor", weaponListElement.elementPanel, -weaponDesc.width - 10, (pickerElement.height - weaponDesc.getHeight()) / 2, pickerElement.elementPanel)
            }

            weaponListElement.onClick {
                if (!it.isLMBEvent) return@onClick

                if (weapon.mountType == WeaponAPI.WeaponType.BALLISTIC || weapon.mountType == WeaponAPI.WeaponType.HYBRID || weapon.mountType == WeaponAPI.WeaponType.COMPOSITE || weapon.mountType == WeaponAPI.WeaponType.UNIVERSAL) {
                    weaponListElement.playSound("ui_refit_slot_filled_ballistic_small", 1f, 1f)
                }
                if (weapon.mountType == WeaponAPI.WeaponType.ENERGY) {
                    weaponListElement.playSound("ui_refit_slot_filled_energy_small", 1f, 1f)
                }
                if (weapon.mountType == WeaponAPI.WeaponType.MISSILE || weapon.mountType == WeaponAPI.WeaponType.SYNERGY) {
                    weaponListElement.playSound("ui_refit_slot_filled_missile_small", 1f, 1f)
                }

                data.fittedWeapons.set(mount.id, weapon.weaponId)
                recreatePanel()
            }

        }
        pickerList.addSpacer(3f)


        pickerElement.elementPanel.addUIElement(pickerList)
        pickerList.position.inTL(0f, 0f)

        /*var plugin = BackgroundPanelPlugin(panel)
        var popupPanel = panel.createCustomPanel(pWidth, pHeight, plugin)
        plugin.panel = popupPanel
        panel.addComponent(popupPanel)

        popupPanel.position.inTL(mountElement.x, 0f)*/

    }

    fun openComponentPicker(componentType: BaseFighterComponent.ComponentType, panel: CustomPanelAPI, element: TooltipMakerAPI, selectorElement: LunaElement, slotId: Int) {

        var data = selectedFighter

        var pWidth = 350f
        var pHeight = 370f

        var components = ComponentPluginLoader.getAllComponents()
        if (componentType == BaseFighterComponent.ComponentType.CHASSIS) components = components.filter { it is BaseFighterChassis }
        if (componentType == BaseFighterComponent.ComponentType.ENGINE) components = components.filter { it is BaseFighterEngine }
        if (componentType == BaseFighterComponent.ComponentType.SUBSYSTEM) components = components.filter { it is BaseFighterSubsystem }

        var pickerElement = ComponentPickerBackgroundElement(this, element, pWidth, pHeight).apply {
            enableTransparency = true
            backgroundColor = Color(0, 0, 0)
            borderAlpha = 1f
            position.rightOfMid(selectorElement.elementPanel, 35f)
        }

        /*pickerElement.onClickOutside {
            it.consume()
            selectedMount = null
            recreatePanel()
        }*/

        var pickerList = pickerElement.elementPanel.createUIElement(pWidth, pHeight, true)
        pickerList.position.inTL(0f, 0f)

        var comp = components.map { Pair(it, FighterStatsObject()).apply { first.applyStats(second) } }.sortedByDescending { it.second.opCost.modifiedValue }

        var first = true
        for ((component, stats) in comp) {
            component.applyStats(stats)

            var componentListElement = ComponentsListElement(stats, component, pickerList, pWidth - 5, 70f).apply {
                enableTransparency = true
                backgroundAlpha = 0.0f
                backgroundColor = Misc.getDarkPlayerColor()
                renderBorder = false

                onHoverEnter {
                    playSound("ui_button_mouseover", 1f, 1f)
                }

            }
            if (first) {
                first = false
                componentListElement.position.inTL(5f, 3f)
            }
            pickerList.addSpacer(3f)


            element.addTooltipTo(ComponentTooltipCreator(component), componentListElement.elementPanel, TooltipMakerAPI.TooltipLocation.LEFT)


            componentListElement.onClick {
                if (!it.isLMBEvent) return@onClick

                componentListElement.playClickSound()
                if (componentType == BaseFighterComponent.ComponentType.CHASSIS) {
                    data.setChassisAndClear(component.getId())
                }
                else if (componentType == BaseFighterComponent.ComponentType.ENGINE) {
                    data.setEngine(component.getId())
                }
                else if (componentType == BaseFighterComponent.ComponentType.SUBSYSTEM) {
                    data.setSubsystemInSlot(slotId, component.getId())
                }

                data.applyDataToSpecs()
                recreatePanel()
            }

        }
        pickerList.addSpacer(3f)


        pickerElement.elementPanel.addUIElement(pickerList)
        pickerList.position.inTL(0f, 0f)

        /*var plugin = BackgroundPanelPlugin(panel)
        var popupPanel = panel.createCustomPanel(pWidth, pHeight, plugin)
        plugin.panel = popupPanel
        panel.addComponent(popupPanel)

        popupPanel.position.inTL(mountElement.x, 0f)*/

    }



    fun startSimulation(wingId: String)
    {
        var fakePlayerFleet = Global.getFactory().createEmptyFleet(Factions.PLAYER, "Test", true)
        Global.getSector().playerFleet.containingLocation.addEntity(fakePlayerFleet)
        fakePlayerFleet.setCircularOrbit(Global.getSector().playerFleet, 0.1f, 0.1f, 0.1f)

        var member = fakePlayerFleet.addShip("drover_Hull", FleetMemberType.SHIP)
        var variant = member.variant

        variant.setWingId(0, wingId)
        variant.setWingId(1, wingId)

        variant.numFluxVents = 300
        variant.numFluxCapacitors = 300

        var enemyFleet = Global.getFactory().createEmptyFleet(Factions.HEGEMONY, "Test", true)
        var enemyMember = enemyFleet.addShip("enforcer_Balanced", FleetMemberType.SHIP)


        val bcc = BattleCreationContext(fakePlayerFleet, FleetGoal.ATTACK, enemyFleet, FleetGoal.ATTACK)
        bcc.aiRetreatAllowed = false
        bcc.objectivesAllowed = false
        bcc.standoffRange = 500f

        /*var state = AppDriver.getInstance().currentState as CampaignState
        state.startBattle(bcc)*/
        CampaignEngine.getInstance().getCampaignUI().startBattle(bcc)

        Global.getSector().playerFleet.containingLocation.removeEntity(fakePlayerFleet)

        Global.getCombatEngine().addPlugin( object : EveryFrameCombatPlugin {

            override fun init(engine: CombatEngineAPI?) {
            }

            override fun processInputPreCoreControls(amount: Float, events: MutableList<InputEventAPI>?) {
                events!!.forEach {
                    if (it.isConsumed) return@forEach
                    if (it.isKeyDownEvent && it.eventValue == Keyboard.KEY_ESCAPE)
                    {
                        if (Global.getCombatEngine()?.isCombatOver == false) {
                            Global.getCombatEngine().endCombat(0f)
                            //it.consume()
                        }
                    }
                }
            }

            override fun advance(amount: Float, events: MutableList<InputEventAPI>?) {
                var playership = Global.getCombatEngine().playerShip
                playership.mutableStats.armorDamageTakenMult.modifyMult("modFighters_sim", 0f)
                playership.mutableStats.hullDamageTakenMult.modifyMult("modFighters_sim", 0f)

                /*var playerManager = Global.getCombatEngine().getFleetManager(0) as CombatFleetManager

                for (member in playerManager.campaignFleet.members) {
                    playerManager.retreated.add(member)
                }

                //ReflectionUtils.invoke("setCampaignFleet", playerManager, fakePlayerFleet)*/
            }

            override fun renderInWorldCoords(viewport: ViewportAPI?) {

            }

            override fun renderInUICoords(viewport: ViewportAPI?) {

            }

        })
        // Global.getCombatEngine().endCombat(10f)
    }

}
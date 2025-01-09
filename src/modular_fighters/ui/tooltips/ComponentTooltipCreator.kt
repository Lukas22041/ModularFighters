package modular_fighters.ui.tooltips

import com.fs.starfarer.api.combat.ShieldAPI
import com.fs.starfarer.api.impl.campaign.ids.Strings
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.BaseTooltipCreator
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.ui.UIComponentAPI
import com.fs.starfarer.api.util.Misc
import com.fs.starfarer.ui.impl.StandardTooltipV2Expandable
import modular_fighters.components.BaseFighterComponent
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.modifier.FighterStatsObject

class ComponentTooltipCreator(var component: BaseFighterComponent) : BaseTooltipCreator() {

    override fun getTooltipWidth(tooltipParam: Any?): Float {
        return 400f
    }

    override fun createTooltip(tooltip: TooltipMakerAPI, expanded: Boolean, tooltipParam: Any?) {

        tooltip.addTitle(component.getName())
        tooltip.addSpacer(10f)

        if (component.getDesignType() != "") {
            var c = Misc.getDesignTypeColor(component.getDesignType())
            tooltip.addPara("Design Type: ${component.getDesignType()}", 0f, Misc.getGrayColor(), c, component.getDesignType())
            tooltip.addSpacer(10f)
        }

        component.addPreTooltip(tooltip)
        tooltip.addSpacer(10f)

        if (component is BaseFighterChassis) addChassisTooltip(tooltip)
        if (component is BaseFighterChassis) addEngineTooltip(tooltip)
        if (component is BaseFighterChassis) addComponentTooltip(tooltip)

        tooltip.addSpacer(10f)
        component.addPostTooltip(tooltip)

    }

    fun addChassisTooltip(tooltip: TooltipMakerAPI) {

        var chassis = component as BaseFighterChassis

        var stats = FighterStatsObject()
        component.applyStats(stats)

        tooltip.addSectionHeading("Chassis Stats", Alignment.MID, 0f)
        tooltip.addSpacer(10f)

        var role = tooltip.addPara("Role", 0f) as UIComponentAPI
        var ordnancePoints = tooltip.addPara("Ordnance points", 0f) as UIComponentAPI

        tooltip.addSpacer(10f)

        var numFighters = tooltip.addPara("Fighters per wing", 0f) as UIComponentAPI
        var refitTime = tooltip.addPara("Base replacement time (seconds)", 0f) as UIComponentAPI
        var crewPerFighter = tooltip.addPara("Crew per fighter", 0f) as UIComponentAPI

        tooltip.addSpacer(10f)

        var hitpoints = tooltip.addPara("Hull integrity", 0f) as UIComponentAPI
        var armor = tooltip.addPara("Armor rating", 0f) as UIComponentAPI
        var shield: UIComponentAPI? = null
        if (chassis.getChassisSpec().shieldType != ShieldAPI.ShieldType.NONE) {
            shield = tooltip.addPara(chassis.getChassisSpec().shieldSpec.type.name.lowercase().capitalize() + " Shield", 0f) as UIComponentAPI
        }

        tooltip.addSpacer(10f)

        var dmgMult = tooltip.addPara("Damage Modifier", 0f) as UIComponentAPI
        var rangeMult = tooltip.addPara("Range Modifier", 0f) as UIComponentAPI
        var speedMult = tooltip.addPara("Speed Modifier", 0f) as UIComponentAPI

        tooltip.addSpacer(10f)

        var independent = tooltip.addPara("Can fly independently", 0f) as UIComponentAPI

        var height = tooltip.heightSoFar



        //Text
        var roleP = tooltip.addPara("${stats.role.name.lowercase().capitalize()}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        roleP.position.rightOfMid(role as UIComponentAPI, -role.position.width + tooltip.widthSoFar - roleP.computeTextWidth(roleP.text))

        var ordnanceP = tooltip.addPara("${stats.opCost.modifiedInt}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        ordnanceP.position.rightOfMid(ordnancePoints as UIComponentAPI, -ordnancePoints.position.width + tooltip.widthSoFar - ordnanceP.computeTextWidth(ordnanceP.text))


        var numFightersP = tooltip.addPara("${stats.numFighters.modifiedInt}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        numFightersP.position.rightOfMid(numFighters as UIComponentAPI, -numFighters.position.width + tooltip.widthSoFar - numFightersP.computeTextWidth(numFightersP.text))

        var refitTimeText = "${stats.refitTime.modifiedInt}"
        if (stats.refitTime.modifiedInt >= 99999) refitTimeText = "Can not be replaced"
        var refitTimeP = tooltip.addPara("$refitTimeText", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        refitTimeP.position.rightOfMid(refitTime as UIComponentAPI, -refitTime.position.width + tooltip.widthSoFar - refitTimeP.computeTextWidth(refitTimeP.text))

        var crewText = "${stats.crewPerFighter.modifiedInt}"
        if (stats.crewPerFighter.modifiedInt == 0) {
            crewText = "Autonomous"
        }
        var crewPerFighterP = tooltip.addPara(crewText, 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        crewPerFighterP.position.rightOfMid(crewPerFighter as UIComponentAPI, -crewPerFighter.position.width + tooltip.widthSoFar - crewPerFighterP.computeTextWidth(crewPerFighterP.text))


        var hitpointsP = tooltip.addPara("${stats.hitpoints.modifiedInt}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        hitpointsP.position.rightOfMid(hitpoints as UIComponentAPI, -hitpoints.position.width + tooltip.widthSoFar - hitpointsP.computeTextWidth(hitpointsP.text))

        var armorP = tooltip.addPara("${stats.armor.modifiedInt}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        armorP.position.rightOfMid(armor as UIComponentAPI, -armor.position.width + tooltip.widthSoFar - armorP.computeTextWidth(armorP.text))

        if (shield != null) {
            var shieldP = tooltip.addPara("${chassis.getChassisSpec().fluxCapacity.toInt()}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
            shieldP.position.rightOfMid(shield as UIComponentAPI, -shield.position.width + tooltip.widthSoFar - shieldP.computeTextWidth(shieldP.text))
        }

        var damageP = tooltip.addPara("${stats.damageMult.modifiedValue}${Strings.X}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        damageP.position.rightOfMid(dmgMult as UIComponentAPI, -dmgMult.position.width + tooltip.widthSoFar - damageP.computeTextWidth(damageP.text))

        var rangeP = tooltip.addPara("${stats.rangeMult.modifiedValue}${Strings.X}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        rangeP.position.rightOfMid(rangeMult as UIComponentAPI, -rangeMult.position.width + tooltip.widthSoFar - rangeP.computeTextWidth(rangeP.text))

        var speedP = tooltip.addPara("${stats.speedMult.modifiedValue}${Strings.X}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        speedP.position.rightOfMid(speedMult as UIComponentAPI, -speedMult.position.width + tooltip.widthSoFar - speedP.computeTextWidth(speedP.text))

        var indepText = "No"
        var indepColor = Misc.getNegativeHighlightColor()
        if (stats.isIndependent) {
            indepText = "Yes"
            indepColor = Misc.getPositiveHighlightColor()
        }

        var independentP = tooltip.addPara(indepText, 0f, indepColor, indepColor )
        independentP.position.rightOfMid(independent as UIComponentAPI, -independent.position.width + tooltip.widthSoFar - independentP.computeTextWidth(independentP.text))


        if (tooltip is StandardTooltipV2Expandable) {
           tooltip.heightSoFar = height
        }

        //tooltip.addPara("", 0f).position.inTL(5f, height)
        //tooltip.addPara("Test", 0f)
    }

    fun addEngineTooltip(tooltip: TooltipMakerAPI) {

    }

    fun addComponentTooltip(tooltip: TooltipMakerAPI) {

    }

}
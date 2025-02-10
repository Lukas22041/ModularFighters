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
import modular_fighters.components.engines.BaseFighterEngine
import modular_fighters.components.subsystems.BaseFighterSubsystem
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
        if (component is BaseFighterEngine) addEngineTooltip(tooltip)
        if (component is BaseFighterSubsystem) addComponentTooltip(tooltip)

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
        var angle = tooltip.addPara("Attacks at an angle", 0f) as UIComponentAPI

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

        var angleText = "No"
        var angleColor = Misc.getNegativeHighlightColor()
        if (stats.attackAtAngle) {
            angleText = "Yes"
            angleColor = Misc.getPositiveHighlightColor()
        }

        var angleP = tooltip.addPara(angleText, 0f, angleColor, angleColor )
        angleP.position.rightOfMid(angle as UIComponentAPI, -angle.position.width + tooltip.widthSoFar - angleP.computeTextWidth(angleP.text))


        if (tooltip is StandardTooltipV2Expandable) {
           tooltip.heightSoFar = height - 10f
        }

        //tooltip.addPara("", 0f).position.inTL(5f, height)
        //tooltip.addPara("Test", 0f)
    }

    fun addEngineTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addSectionHeading("Engine Stats", Alignment.MID, 0f)
        tooltip.addSpacer(10f)

        var engine = component as BaseFighterEngine

        var stats = FighterStatsObject()
        component.applyStats(stats)

        var topSpeed = stats.topSpeed.modifiedInt
        var acceleration = stats.acceleration.modifiedInt
        var deceleration = stats.deceleration.modifiedInt
        var turnRate = stats.maxTurnRate.modifiedInt
        var turnAcceleration = stats.turnAcceleration.modifiedInt

        var topSpeedPara = tooltip.addPara("Top Speed", 0f) as UIComponentAPI
        tooltip.addSpacer(10f)
        var accelerationPara = tooltip.addPara("Acceleration", 0f) as UIComponentAPI
        var decelerationPara = tooltip.addPara("Deceleration", 0f) as UIComponentAPI
        tooltip.addSpacer(10f)
        var maxTurnRatePara = tooltip.addPara("Max Turn rate", 0f) as UIComponentAPI
        var turnAccelerationPara = tooltip.addPara("Turn Acceleration", 0f) as UIComponentAPI

        var height = tooltip.heightSoFar

        var topSpeedParaN = tooltip.addPara("$topSpeed", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        topSpeedParaN.position.rightOfMid(topSpeedPara as UIComponentAPI, -topSpeedPara.position.width + tooltip.widthSoFar - topSpeedParaN.computeTextWidth(topSpeedParaN.text))

        var accelerationParaN = tooltip.addPara("$acceleration", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        accelerationParaN.position.rightOfMid(accelerationPara as UIComponentAPI, -accelerationPara.position.width + tooltip.widthSoFar - accelerationParaN.computeTextWidth(accelerationParaN.text))

        var decelerationParaN = tooltip.addPara("$deceleration", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        decelerationParaN.position.rightOfMid(decelerationPara as UIComponentAPI, -decelerationPara.position.width + tooltip.widthSoFar - decelerationParaN.computeTextWidth(decelerationParaN.text))

        var maxTurnRateParaN = tooltip.addPara("$turnRate", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        maxTurnRateParaN.position.rightOfMid(maxTurnRatePara as UIComponentAPI, -maxTurnRatePara.position.width + tooltip.widthSoFar - maxTurnRateParaN.computeTextWidth(maxTurnRateParaN.text))

        var turnAccelerationRateParaN = tooltip.addPara("$turnAcceleration", 0f, Misc.getHighlightColor(), Misc.getHighlightColor() )
        turnAccelerationRateParaN.position.rightOfMid(turnAccelerationPara as UIComponentAPI, -turnAccelerationPara.position.width + tooltip.widthSoFar - turnAccelerationRateParaN.computeTextWidth(turnAccelerationRateParaN.text))

        if (tooltip is StandardTooltipV2Expandable) {
            tooltip.heightSoFar = height - 10f
        }
    }

    fun addComponentTooltip(tooltip: TooltipMakerAPI) {

    }

}
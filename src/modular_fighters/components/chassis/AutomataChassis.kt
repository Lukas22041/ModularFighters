package modular_fighters.components.chassis

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin
import com.fs.starfarer.api.combat.FighterLaunchBayAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.loading.WingRole
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.modifier.FighterStatsObject
import java.awt.Color

class AutomataChassis : BaseFighterChassis() {
    override fun getChassisSpecId(): String {
       return "chassis_automata"
    }

    override fun getName(): String {
        return "Automata Chassis"
    }

    override fun getDesignType(): String {
        return "Remnant"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("The automata is a class of its own, capable of acting entirely independently of its host ship.", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.role = WingRole.FIGHTER
        stats.opCost.modifyFlat(getId(), 4f)

        stats.numFighters.modifyFlat(getId(), 1f)
        stats.refitTime.modifyFlat(getId(), 15f)
        stats.crewPerFighter.modifyFlat(getId(), 0f)

        stats.hitpoints.modifyFlat(getId(), 750f)
        stats.armor.modifyFlat(getId(), 120f)

        stats.damageMult.modifyMult(getId(), 1.5f)
        stats.rangeMult.modifyMult(getId(), 1f)
        stats.speedMult.modifyMult(getId(), 1f)

        stats.baseValue.modifyFlat(getId(), 1000f)

        stats.turnAcceleration.modifyMult(getId(), 0.8f) //Automata Specific
        stats.maxTurnRate.modifyMult(getId(), 0.8f) //Automata Specific
        stats.deceleration.modifyMult(getId(), 0.20f) //Automata Specific

        stats.isIndependent = true

    }

    override fun getOutlineColor1(): Color {
        return Color(3, 252, 186)
    }

    override fun getOutlineColor2(): Color {
        return Color(3, 252, 186)
    }

    override fun getOultineAlpha(): Float {
        return 0.33f
    }

}

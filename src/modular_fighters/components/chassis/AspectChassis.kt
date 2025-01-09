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

class AspectChassis : BaseFighterChassis() {
    override fun getChassisSpecId(): String {
       return "chassis_aspect"
    }

    override fun getName(): String {
        return "Aspect Chassis"
    }

    override fun getDesignType(): String {
        return "Unknown"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("A unique chassis that split of from the remains of an alien type of design. " +
                "It's nimble, heavily armored and acts independently of its carrier, but can not be replaced during combat if destroyed. ", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.role = WingRole.FIGHTER
        stats.opCost.modifyFlat(getId(), 4f)

        stats.numFighters.modifyFlat(getId(), 4f)
        stats.refitTime.modifyFlat(getId(), 100000f)
        stats.crewPerFighter.modifyFlat(getId(), 0f)

        //Slightly worse than vanilla aspects
        stats.hitpoints.modifyFlat(getId(), 1000f)
        stats.armor.modifyFlat(getId(), 400f)

        stats.damageMult.modifyMult(getId(), 0.75f)
        stats.rangeMult.modifyMult(getId(), 1f)
        stats.speedMult.modifyMult(getId(), 1.2f)

        stats.baseValue.modifyFlat(getId(), 4000f)

        stats.isIndependent = true
        stats.isIndependentNoReturn = true

    }

}

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

class HuntressChassis : BaseFighterChassis() {
    override fun getChassisSpecId(): String {
       return "chassis_huntress"
    }

    override fun getName(): String {
        return "Huntress Chassis"
    }

    override fun getDesignType(): String {
        return "Midline"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("Tough, agile and versatile, the Huntress-class chassis makes for a solid fighter base for a large variety of potential designs.", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.role = WingRole.FIGHTER
        stats.opCost.modifyFlat(getId(), 2f)

        stats.numFighters.modifyFlat(getId(), 3f)
        stats.refitTime.modifyFlat(getId(), 10f)
        stats.crewPerFighter.modifyFlat(getId(), 2f)

        stats.hitpoints.modifyFlat(getId(), 750f)
        stats.armor.modifyFlat(getId(), 100f)

        stats.damageMult.modifyMult(getId(), 1f)
        stats.rangeMult.modifyMult(getId(), 1f)
        stats.speedMult.modifyMult(getId(), 1f)

        stats.baseValue.modifyFlat(getId(), 1000f)

        stats.isIndependent = false

    }

    override fun getOutlineColor1(): Color {
        return Color(201, 162, 32)
    }

    override fun getOutlineColor2(): Color {
        return Color(201, 162, 32)
    }

    override fun getOultineAlpha(): Float {
        return 0.75f
    }

}

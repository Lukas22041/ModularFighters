package modular_fighters.components.chassis

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin
import com.fs.starfarer.api.combat.FighterLaunchBayAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.loading.FormationType
import com.fs.starfarer.api.loading.WingRole
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.modifier.FighterStatsObject
import java.awt.Color

class GoliathChassis : BaseFighterChassis() {
    override fun getChassisSpecId(): String {
       return "chassis_goliath"
    }

    override fun getName(): String {
        return "Goliath Chassis"
    }

    override fun getDesignType(): String {
        return "Low Tech"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("The goliath stands out compared to other fighters, unlike most other wings, all of its power is located in a single chunk of hull. Without a shield generator, it relies on its own armor to survive.", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.role = WingRole.FIGHTER
        stats.formation = FormationType.V
        stats.opCost.modifyFlat(getId(), 3f)

        stats.numFighters.modifyFlat(getId(), 1f)
        stats.refitTime.modifyFlat(getId(), 12f)
        stats.crewPerFighter.modifyFlat(getId(), 3f)

        stats.hitpoints.modifyFlat(getId(), 1250f)
        stats.armor.modifyFlat(getId(), 200f)

        stats.damageMult.modifyMult(getId(), 1.5f)
        stats.rangeMult.modifyMult(getId(), 1f)
        stats.speedMult.modifyMult(getId(), 0.75f)

        stats.baseValue.modifyFlat(getId(), 1000f)

        stats.isIndependent = false

    }

    override fun getOutlineColor1(): Color {
        return Color(171, 171, 171)
    }

    override fun getOutlineColor2(): Color {
        return Color(171, 171, 171)
    }

    override fun getOultineAlpha(): Float {
        return 0.5f
    }

}

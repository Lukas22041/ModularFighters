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

class BayonetChassis : BaseFighterChassis() {
    override fun getChassisSpecId(): String {
       return "chassis_bayonet"
    }

    override fun getName(): String {
        return "Bayonet Chassis"
    }

    override fun getDesignType(): String {
        return "High Tech"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("The bayonet offers itself as a versatile missile platform perfect for delivering quick missile salvos.", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.role = WingRole.BOMBER
        stats.formation = FormationType.V
        stats.opCost.modifyFlat(getId(), 10f)

        stats.numFighters.modifyFlat(getId(), 2f)
        stats.refitTime.modifyFlat(getId(), 12f)
        stats.crewPerFighter.modifyFlat(getId(), 2f)

        stats.hitpoints.modifyFlat(getId(), 500f)
        stats.armor.modifyFlat(getId(), 25f)

        stats.damageMult.modifyMult(getId(), 1f)
        stats.rangeMult.modifyMult(getId(), 1f)
        stats.speedMult.modifyMult(getId(), 0.8f)

        stats.baseValue.modifyFlat(getId(), 1000f)

        stats.isIndependent = false

    }

    override fun getOutlineColor1(): Color {
        return Color(235, 52, 82)
    }

    override fun getOutlineColor2(): Color {
        return Color(135,206,255)
    }

    override fun getOultineAlpha(): Float {
        return 0.33f
    }

}

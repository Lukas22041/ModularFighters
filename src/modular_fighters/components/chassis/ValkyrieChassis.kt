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

class ValkyrieChassis : BaseFighterChassis() {
    override fun getChassisSpecId(): String {
       return "chassis_valkyrie"
    }

    override fun getName(): String {
        return "Valkyrie Chassis"
    }

    override fun getDesignType(): String {
        return "Midline"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("The valkyrie uses quick maneuvers to overwhelm its target, performing attack runs from a further distance to stay out of the range of fire.", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.role = WingRole.FIGHTER
        stats.formation = FormationType.V
        stats.opCost.modifyFlat(getId(), 3f)

        stats.numFighters.modifyFlat(getId(), 2f)
        stats.refitTime.modifyFlat(getId(), 10f)
        stats.crewPerFighter.modifyFlat(getId(), 2f)

        stats.hitpoints.modifyFlat(getId(), 600f)
        stats.armor.modifyFlat(getId(), 80f)

        stats.damageMult.modifyMult(getId(), 1.25f)
        stats.rangeMult.modifyMult(getId(), 1f)
        stats.speedMult.modifyMult(getId(), 1.25f)

        stats.baseValue.modifyFlat(getId(), 1000f)

        stats.isIndependent = false
        stats.attackAtAngle = true

    }

    override fun getOutlineColor1(): Color {
        return Color(30, 30, 30)
    }

    override fun getOutlineColor2(): Color {
        return Color(30, 30, 30)
    }

    override fun getOultineAlpha(): Float {
        return 0.75f
    }

}

package modular_fighters.components.chassis

import com.fs.starfarer.api.loading.WingRole
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.modifier.FighterStatsObject

class AspectChassis : BaseFighterChassis() {
    override fun getChassisSpecId(): String {
       return "chassis_aspect"
    }

    override fun getName(): String {
        return "Damaged Aspect Chassis"
    }

    override fun getDesignType(): String {
        return "Unknown"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("A unique chassis that split of from the remains of an alien type of design. " +
                "A nimble but armored hull that compared to its original counterpart is missing some functionalities due to malfunctions in the blueprint. ", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.role = WingRole.FIGHTER
        stats.opCost.modifyFlat(getId(), 8f)

        stats.numFighters.modifyFlat(getId(), 4f)
        stats.refitTime.modifyFlat(getId(), 20f)
        stats.crewPerFighter.modifyFlat(getId(), 0f)

        //Slightly worse than vanilla aspects
        stats.hitpoints.modifyFlat(getId(), 600f)
        stats.armor.modifyFlat(getId(), 300f)

        stats.damageMult.modifyMult(getId(), 0.5f)
        stats.rangeMult.modifyMult(getId(), 1f)
        stats.speedMult.modifyMult(getId(), 1.2f)

        stats.baseValue.modifyFlat(getId(), 4000f)

        //stats.isIndependent = true //cant do that since its multiple fighters.

    }

}
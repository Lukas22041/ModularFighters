package modular_fighters.components.engines

import com.fs.starfarer.api.loading.FormationType
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.modifier.FighterStatsObject

class AntiqueAntimatterEngine : BaseFighterEngine() {
    override fun getName(): String {
        return "Antique Antimatter Engine"
    }

    override fun getDesignType(): String {
        return "Antique"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("An outdated antimatter drive design. Unused in most parts of the sector, but cheap to implement.", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun getEngineIcon() : String = "graphics/engines/antique_antimatter_engine.png"

    override fun applyStats(stats: FighterStatsObject) {

        stats.engagementRange.modifyFlat(getId(), 4000f)
        stats.opCost.modifyFlat(getId(), 0f)

        stats.topSpeed.modifyFlat(getId(), 150f)
        stats.acceleration.modifyFlat(getId(), 250f)
        stats.deceleration.modifyFlat(getId(), 200f)
        stats.maxTurnRate.modifyFlat(getId(), 70f)
        stats.turnAcceleration.modifyFlat(getId(), 150f)
    }


}
package modular_fighters.components.engines

import com.fs.starfarer.api.loading.FormationType
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.modifier.FighterStatsObject

class AntimatterEngine : BaseFighterEngine() {
    override fun getName(): String {
        return "Antimatter Engine"
    }

    override fun getDesignType(): String {
        return "Common"
    }

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("A standard issue antimatter drive.", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun getEngineIcon() : String = "graphics/engines/antimatter_engine.png"

    override fun applyStats(stats: FighterStatsObject) {

        stats.engagementRange.modifyFlat(getId(), 4000f)
        stats.opCost.modifyFlat(getId(), 1f)

        stats.topSpeed.modifyFlat(getId(), 200f)
        stats.acceleration.modifyFlat(getId(), 300f)
        stats.deceleration.modifyFlat(getId(), 250f)
        stats.maxTurnRate.modifyFlat(getId(), 90f)
        stats.turnAcceleration.modifyFlat(getId(), 180f)

    }


}
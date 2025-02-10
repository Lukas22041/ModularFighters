package modular_fighters.components.engines

import com.fs.starfarer.api.loading.FormationType
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.modifier.FighterStatsObject

class CryomatterEngine : BaseFighterEngine() {
    override fun getName(): String {
        return "Cryomatter Engine"
    }

    override fun getDesignType(): String {
        return "Unknown"
    }

    override fun getEngineIcon() : String = "graphics/engines/cryomatter_engine.png"

    override fun addPreTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("A drive of unknown origin, the laws of physics being broken in every component.", 0f, Misc.getTextColor(), Misc.getHighlightColor())
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.engagementRange.modifyFlat(getId(), 4000f)
        stats.opCost.modifyFlat(getId(), 3f)

        stats.topSpeed.modifyFlat(getId(), 350f)
        stats.acceleration.modifyFlat(getId(), 350f)
        stats.deceleration.modifyFlat(getId(), 350f)
        stats.maxTurnRate.modifyFlat(getId(), 120f)
        stats.turnAcceleration.modifyFlat(getId(), 360f)

    }


}
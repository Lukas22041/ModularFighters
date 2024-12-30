package modular_fighters.components.engines

import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.modifier.FighterStatsObject

class DebugEngine : BaseFighterEngine() {
    override fun getName(): String {
        return "Debug Engine"
    }

    override fun addComponentTooltip(tooltip: TooltipMakerAPI) {

    }

    override fun applyStats(stats: FighterStatsObject) {

    }


}
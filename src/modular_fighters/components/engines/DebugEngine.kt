package modular_fighters.components.engines

import com.fs.starfarer.api.loading.FormationType
import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.modifier.FighterStatsObject

class DebugEngine : BaseFighterEngine() {
    override fun getName(): String {
        return "Debug Engine"
    }

    override fun addComponentTooltip(tooltip: TooltipMakerAPI) {

    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.formation = FormationType.V
        stats.engagementRange.modifyFlat(getId(), 4000f)

        stats.topSpeed.modifyFlat(getId(), 300f)
        stats.acceleration.modifyFlat(getId(), 350f)
        stats.deceleration.modifyFlat(getId(), 350f)
        stats.maxTurnRate.modifyFlat(getId(), 120f)
        stats.turnAcceleration.modifyFlat(getId(), 360f)
    }


}
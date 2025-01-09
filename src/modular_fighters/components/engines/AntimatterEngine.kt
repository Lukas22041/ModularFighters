package modular_fighters.components.engines

import com.fs.starfarer.api.loading.FormationType
import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.modifier.FighterStatsObject

class AntimatterEngine : BaseFighterEngine() {
    override fun getName(): String {
        return "Antimatter Engine"
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.formation = FormationType.V
        stats.engagementRange.modifyFlat(getId(), 4000f)
        stats.opCost.modifyFlat(getId(), 1f)

        stats.topSpeed.modifyFlat(getId(), 200f)
        stats.acceleration.modifyFlat(getId(), 300f)
        stats.deceleration.modifyFlat(getId(), 250f)
        stats.maxTurnRate.modifyFlat(getId(), 90f)
        stats.turnAcceleration.modifyFlat(getId(), 180f)

        stats.attackAtAngle = false

    }


}
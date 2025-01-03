package modular_fighters.components.engines

import com.fs.starfarer.api.loading.FormationType
import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.modifier.FighterStatsObject

class CryomatterEngine : BaseFighterEngine() {
    override fun getName(): String {
        return "Debug Engine"
    }

    override fun applyStats(stats: FighterStatsObject) {

        stats.formation = FormationType.BOX
        stats.engagementRange.modifyFlat(getId(), 4000f)
        stats.opCost.modifyFlat(getId(), 4f)

        stats.topSpeed.modifyFlat(getId(), 350f)
        stats.acceleration.modifyFlat(getId(), 350f)
        stats.deceleration.modifyFlat(getId(), 350f)
        stats.maxTurnRate.modifyFlat(getId(), 120f)
        stats.turnAcceleration.modifyFlat(getId(), 360f)

        stats.attackAtAngle = true

    }


}
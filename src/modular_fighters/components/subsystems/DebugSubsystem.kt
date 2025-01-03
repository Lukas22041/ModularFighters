package modular_fighters.components.subsystems

import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.modifier.FighterStatsObject

class DebugSubsystem : BaseFighterSubsystem() {
    override fun isShipSystem() : Boolean {
        return false
    }

    override fun getName(): String {
        return "Debug Subsystem"
    }

    override fun applyStats(stats: FighterStatsObject) {

    }

}
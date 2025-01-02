package modular_fighters.components.chassis

import modular_fighters.modifier.FighterStatsObject

class DebugChassis : BaseFighterChassis() {
    override fun getChassisSpecId(): String {
       return "debug_chassis"
    }

    override fun getWingSize(): Int {
        return 1
    }

    override fun getName(): String {
        return "Debug Chassis"
    }

    override fun applyStats(stats: FighterStatsObject) {
        stats.numFighters.modifyFlat(getId(), 3f)
    }

}
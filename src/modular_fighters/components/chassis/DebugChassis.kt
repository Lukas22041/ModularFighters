package modular_fighters.components.chassis

import com.fs.starfarer.api.loading.FormationType
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
        stats.refitTime.modifyFlat(getId(), 10f)
        stats.crewPerFighter.modifyFlat(getId(), 1f)

        stats.hitpoints.modifyFlat(getId(), 600f)
        stats.armor.modifyFlat(getId(), 250f)

        stats.attackRunRange.modifyFlat(getId(), 600f)
        stats.baseValue.modifyFlat(getId(), 4000f)
    }

}
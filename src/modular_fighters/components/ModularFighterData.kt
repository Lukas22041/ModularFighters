package modular_fighters.components

import com.fs.starfarer.api.Global
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.components.chassis.DebugChassis
import modular_fighters.components.engines.BaseFighterEngine
import modular_fighters.components.engines.DebugEngine
import modular_fighters.components.subsystems.BaseFighterSubsystem

class ModularFighterData(var fighterSpecId: String, var fighterWingSpecId: String, var name: String) {

    var chassis: BaseFighterChassis = DebugChassis()
    var engine: BaseFighterEngine = DebugEngine()
    var subsystems = ArrayList<BaseFighterSubsystem>()

    fun getSpec() = Global.getSettings().getHullSpec(fighterSpecId)
    fun getWingSpec() = Global.getSettings().getFighterWingSpec(fighterWingSpecId)

}
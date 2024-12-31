package modular_fighters

import com.fs.starfarer.api.Global
import modular_fighters.components.ModularFighterData

class ModData {

    //SpecId, Data
    var fighterData = HashMap<String, ModularFighterData>()
    var visibleEntries = 1

    init {
        var specs = Global.getSettings().allShipHullSpecs.filter { it.baseHullId.contains("modularFighterSpec") }.filter { !it.isDHull }
        var index = 0
        for (spec in specs) {
            index++
            var data = ModularFighterData(spec.baseHullId, spec.baseHullId + "_wing", "Modular LPC $index")
            fighterData.put(spec.baseHullId, data)
        }
    }


}
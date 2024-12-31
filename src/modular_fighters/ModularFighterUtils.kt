package modular_fighters

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.ShipHullSpecAPI
import modular_fighters.components.ModularFighterData

object ModularFighterUtils {

    var dataKey = "\$modular_fighters_data"

    @JvmStatic
    fun getData() : ModData {
        var data = Global.getSector().memoryWithoutUpdate.get(dataKey) as ModData?
        if (data == null) {
            data = ModData()
            Global.getSector().memoryWithoutUpdate.set(dataKey, data)
        }
        return data
    }

    fun updateSpecsToMatchData() {
        val data = getData()

        for ((spec, fighterData) in data.fighterData) {
            updateSpecToMatchData(Global.getSettings().getHullSpec(spec), fighterData)
        }
    }

    fun updateSpecToMatchData(spec: ShipHullSpecAPI, fighterData: ModularFighterData) {

    }

}
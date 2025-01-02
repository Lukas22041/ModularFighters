package modular_fighters.hullmods

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.BaseHullMod
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.ModularFighterUtils
import modular_fighters.misc.baseOrModSpec

class ModularFighterHullmod : BaseHullMod() {

    override fun shouldAddDescriptionToTooltip(hullSize: ShipAPI.HullSize?, ship: ShipAPI?,isForModSpec: Boolean): Boolean {
        return false
    }

    override fun addPostDescriptionSection(tooltip: TooltipMakerAPI?, hullSize: ShipAPI.HullSize?, ship: ShipAPI?, width: Float, isForModSpec: Boolean) {
        tooltip!!.addSpacer(10f)
        tooltip!!.addPara("A fighter that has been designed from a set of unique components.", 0f)
    }

    override fun applyEffectsBeforeShipCreation(hullSize: ShipAPI.HullSize?, stats: MutableShipStatsAPI?, id: String?) {
        var spec = stats?.fleetMember?.baseOrModSpec() ?: return
        var data = ModularFighterUtils.getData().fighterData.get(spec.baseHullId) ?: return
        var wingSpec = Global.getSettings().getFighterWingSpec(data.fighterWingSpecId) ?: return

        if (data.lastStatsObject.isIndependent) {
            wingSpec.addTag("rapid_reform")
            wingSpec.addTag("leader_no_swarm")
            wingSpec.addTag("wingmen_no_swarm")
            wingSpec.addTag("match_leader_facing")
            wingSpec.addTag("attack_at_an_angle")
            wingSpec.addTag("independent_of_carrier")
        }


    }

    override fun applyEffectsAfterShipCreation(ship: ShipAPI?, id: String?) {

    }

}
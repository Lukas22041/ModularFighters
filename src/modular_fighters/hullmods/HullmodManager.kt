package modular_fighters.hullmods

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.BaseHullMod
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.ModularFighterUtils
import modular_fighters.misc.baseOrModSpec

class HullmodManager : BaseHullMod() {

    override fun shouldAddDescriptionToTooltip(hullSize: ShipAPI.HullSize?, ship: ShipAPI?,isForModSpec: Boolean): Boolean {
        return false
    }


    override fun addPostDescriptionSection(tooltip: TooltipMakerAPI?, hullSize: ShipAPI.HullSize?, ship: ShipAPI?, width: Float, isForModSpec: Boolean) {

    }

    override fun applyEffectsBeforeShipCreation(hullSize: ShipAPI.HullSize?, stats: MutableShipStatsAPI?, id: String?) {
        var variant = stats?.variant ?: return
        if (variant.fittedWings.any { it.contains("modularFighterSpec") }) {
            if (!variant.hasHullMod("modular_fighters_nonstandard")) {
                variant.addPermaMod("modular_fighters_nonstandard")
            }
        } else if (variant.hasHullMod("modular_fighters_nonstandard")) {
            variant.removePermaMod("modular_fighters_nonstandard")
        }
    }

    override fun applyEffectsAfterShipCreation(ship: ShipAPI?, id: String?) {

    }

}
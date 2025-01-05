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

class NonStandardFighterDesign : BaseHullMod() {

    override fun shouldAddDescriptionToTooltip(hullSize: ShipAPI.HullSize?, ship: ShipAPI?,isForModSpec: Boolean): Boolean {
        return false
    }

    fun getDP(variant: ShipVariantAPI) : Float {
        var dp = 0f
        for (wing in variant.fittedWings) {
            if (wing.contains("modularFighterSpec")) {
                dp += 1
            }
        }
        return dp
    }

    override fun addPostDescriptionSection(tooltip: TooltipMakerAPI?, hullSize: ShipAPI.HullSize?, ship: ShipAPI?, width: Float, isForModSpec: Boolean) {
        tooltip!!.addSpacer(10f)
        tooltip!!.addPara("This ship is equipped with non-standard, modular fighter chassis which have increased maintenance requirements compared to standard designs, and as such increase the deployment cost of the ship by 1 per LPC.", 0f,
        Misc.getTextColor(), Misc.getHighlightColor(), "modular fighter chassis", "1")
    }

    override fun applyEffectsBeforeShipCreation(hullSize: ShipAPI.HullSize?, stats: MutableShipStatsAPI?, id: String?) {
        var variant = stats?.variant ?: return
        var dp = getDP(variant)
        stats.dynamic.getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, dp)
    }

    override fun applyEffectsAfterShipCreation(ship: ShipAPI?, id: String?) {

    }

}
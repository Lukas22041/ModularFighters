package modular_fighters.misc

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.combat.ShipVariantAPI
import com.fs.starfarer.api.fleet.FleetMemberAPI
import com.fs.starfarer.api.impl.campaign.ids.HullMods
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import lunalib.lunaRefit.BaseRefitButton
import modular_fighters.ui.LPCDesignerPanel

class ModularFightersRefitButton : BaseRefitButton() {

    override fun getButtonName(member: FleetMemberAPI?, variant: ShipVariantAPI?): String? {
        return "LPC Designer"
    }

    override fun getIconName(member: FleetMemberAPI?, variant: ShipVariantAPI?): String? {
        return "graphics/icons/modular_fighters_refit_button.png"
    }

    override fun getOrder(member: FleetMemberAPI?, variant: ShipVariantAPI?): Int {
        return 120
    }

    override fun hasTooltip(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }

    override fun getToolipWidth(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Float {
        return 350f
    }

    override fun addTooltip(tooltip: TooltipMakerAPI, member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?) {

        tooltip.addPara("Design custom LPCs and craft them. You can have up to 50 different designs per save. Once finalized, an LPC can no longer be modified.",
            0f, Misc.getTextColor(), Misc.getHighlightColor(), "custom LPCs", "craft", "50")

    }

    override fun onClick(member: FleetMemberAPI, variant: ShipVariantAPI, event: InputEventAPI?, market: MarketAPI?) {

    }

    override fun hasPanel(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?): Boolean {
        return true
    }

    override fun getPanelWidth(member: FleetMemberAPI?, variant: ShipVariantAPI?): Float {
        return 800f
    }

    override fun getPanelHeight(member: FleetMemberAPI?, variant: ShipVariantAPI?): Float {
        return 500f
    }

    override fun initPanel(backgroundPanel: CustomPanelAPI?, member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?) {
        var creator = LPCDesignerPanel(backgroundPanel!!)
        creator.init()
    }

    override fun onPanelClose(member: FleetMemberAPI?, variant: ShipVariantAPI?, market: MarketAPI?) {

    }
}
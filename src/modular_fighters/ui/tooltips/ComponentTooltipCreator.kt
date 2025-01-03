package modular_fighters.ui.tooltips

import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.BaseTooltipCreator
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.components.BaseFighterComponent
import modular_fighters.components.chassis.BaseFighterChassis

class ComponentTooltipCreator(var component: BaseFighterComponent) : BaseTooltipCreator() {

    override fun getTooltipWidth(tooltipParam: Any?): Float {
        return 400f
    }

    override fun createTooltip(tooltip: TooltipMakerAPI, expanded: Boolean, tooltipParam: Any?) {

        tooltip.addTitle(component.getName())
        tooltip.addSpacer(10f)

        if (component.getDesignType() != "") {
            var c = Misc.getDesignTypeColor(component.getDesignType())
            tooltip.addPara("Design Type: ${component.getDesignType()}", 0f, Misc.getGrayColor(), c, component.getDesignType())
            tooltip.addSpacer(10f)
        }

        component.addPreTooltip(tooltip)
        tooltip.addSpacer(10f)

        if (component is BaseFighterChassis) addChassisTooltip(tooltip)
        if (component is BaseFighterChassis) addEngineTooltip(tooltip)
        if (component is BaseFighterChassis) addComponentTooltip(tooltip)

        tooltip.addSpacer(10f)
        component.addPostTooltip(tooltip)

    }

    fun addChassisTooltip(tooltip: TooltipMakerAPI) {

        tooltip.addSectionHeading("Chassis Stats", Alignment.MID, 0f)
        tooltip.addSpacer(10f)

    }

    fun addEngineTooltip(tooltip: TooltipMakerAPI) {

    }

    fun addComponentTooltip(tooltip: TooltipMakerAPI) {

    }

}
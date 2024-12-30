package modular_fighters.components.chassis

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.components.BaseFighterComponent

abstract class BaseFighterChassis : BaseFighterComponent() {

    fun addTooltip(tooltip: TooltipMakerAPI) {

    }

    override fun addComponentTooltip(tooltip: TooltipMakerAPI) {
        //Stuff for all chassis tooltips here
        addTooltip(tooltip) //Call Individual Tooltip
        //Postfix here
    }

    /* Chassis have their own ship specs for a variety of technical reasons */
    abstract fun getChassisSpecId() : String

    abstract fun getWingSize() : Int

    fun getChassisSpec() = Global.getSettings().getHullSpec(getChassisSpecId())

    fun getSpriteName() = getChassisSpec().spriteName

}
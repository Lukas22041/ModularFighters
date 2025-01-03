package modular_fighters.components.chassis

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.components.BaseFighterComponent
import java.awt.Color

abstract class BaseFighterChassis : BaseFighterComponent() {



    /* Chassis have their own ship specs for a variety of technical reasons */
    abstract fun getChassisSpecId() : String

    fun getChassisSpec() = Global.getSettings().getHullSpec(getChassisSpecId())

    fun getSpriteName() = getChassisSpec().spriteName

    fun getOutlineColor1() : Color = Color(0, 255, 90)
    fun getOutlineColor2() : Color = Color(0, 90, 255)

    /*fun getOutlineColor1() : Color = Color(255, 0, 90)
    fun getOutlineColor2() : Color = Color(0, 90, 255)*/

}
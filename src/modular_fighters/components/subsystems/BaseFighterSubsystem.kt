package modular_fighters.components.subsystems

import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.components.BaseFighterComponent

abstract class BaseFighterSubsystem : BaseFighterComponent() {

    abstract fun isShipSystem() : Boolean

    fun getShipSystemId() : String { return "" }

}
package modular_fighters.components

import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.modifier.FighterStatsObject

abstract class BaseFighterComponent {

    lateinit var spec: ComponentPluginLoader.ComponentSpec

    fun getId() = spec.id

    abstract fun getName() : String

    abstract fun addComponentTooltip(tooltip: TooltipMakerAPI)

    abstract fun applyStats(stats: FighterStatsObject)

    fun applyAfterFighterCreation(ship: ShipAPI) { }

}
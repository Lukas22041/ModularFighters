package modular_fighters.components

import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.modifier.FighterStatsObject

abstract class BaseFighterComponent {

    lateinit var spec: ComponentPluginLoader.ComponentSpec

    fun getId() = spec.id

    abstract fun getName() : String

    abstract fun applyStats(stats: FighterStatsObject)

    open fun getDesignType(): String = ""

    open fun applyAfterFighterCreation(ship: ShipAPI) { }

    open fun addPreTooltip(tooltip: TooltipMakerAPI) {

    }

    open fun addPostTooltip(tooltip: TooltipMakerAPI) {

    }

}
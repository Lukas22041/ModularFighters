package modular_fighters.components

import com.fs.starfarer.api.ui.TooltipMakerAPI
import modular_fighters.modifier.FighterStatsObject

abstract class BaseFighterComponent {

    abstract fun getName()

    abstract fun addComponentTooltip(tooltip: TooltipMakerAPI)

    abstract fun applyStats(stats: FighterStatsObject)
}
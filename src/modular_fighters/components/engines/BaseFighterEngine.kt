package modular_fighters.components.engines

import modular_fighters.components.BaseFighterComponent

abstract class BaseFighterEngine : BaseFighterComponent() {

    open fun getEngineIcon() : String = "graphics/hullmods/augmented_engines.png"

}
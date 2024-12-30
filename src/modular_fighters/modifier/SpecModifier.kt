package modular_fighters.modifier

import com.fs.starfarer.api.combat.ShipHullSpecAPI
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.misc.ReflectionUtils

object SpecModifier {

    //Mounts, Engines, Hullstyle, Sprite, Bounds, Shields
    //Bounds seem to be handled by the sprite spec, colission radius however is just set high by default in the spec.
    fun applyChassisToSpec(spec: ShipHullSpecAPI, chassis: BaseFighterChassis) {
        var chassisSpec = chassis.getChassisSpec()

        //Sprite
        ReflectionUtils.invoke("setSpriteSpec", spec, ReflectionUtils.invoke("getSpriteSpec", chassisSpec))


        //EngineSpec //Do this later, as the engine component should set the defaults
        //ReflectionUtils.invoke("setEngineSpec", spec, ReflectionUtils.invoke("getEngineSpec", chassisSpec))

        //FluxSpec

        //Engines
        var engines = ReflectionUtils.invoke("getEngineSlots", spec) as MutableList<Any>
        var chassisEngines = ReflectionUtils.invoke("getEngineSlots", chassisSpec) as MutableList<Any>
        engines.clear()
        engines.addAll(chassisEngines)

        //Slots
        var slots = ReflectionUtils.invoke("getAllWeaponSlots", spec) as MutableList<Any>
        var chassisSlots = ReflectionUtils.invoke("getAllWeaponSlots", chassisSpec) as MutableList<Any>
        slots.clear()
        slots.addAll(chassisSlots)

        //Hullstyle
        ReflectionUtils.invoke("setHullStyle", spec, ReflectionUtils.invoke("getHullStyle", chassisSpec))

        //Shield
        ReflectionUtils.invoke("setShieldSpec", spec, ReflectionUtils.invoke("getShieldSpec", chassisSpec))

    }

}
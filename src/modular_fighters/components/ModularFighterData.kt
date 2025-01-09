package modular_fighters.components

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.ShipHullSpecAPI
import com.fs.starfarer.api.loading.WeaponGroupSpec
import com.fs.starfarer.api.loading.WeaponGroupType
import com.fs.starfarer.api.loading.WingRole
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.components.engines.BaseFighterEngine
import modular_fighters.components.subsystems.BaseFighterSubsystem
import modular_fighters.misc.ReflectionUtils
import modular_fighters.misc.setArmorRating
import modular_fighters.misc.setHitpoints
import modular_fighters.modifier.FighterStatsObject
import org.lazywizard.lazylib.MathUtils

class ModularFighterData(var fighterSpecId: String, var fighterWingSpecId: String, var variantId: String, var name: String) {

    var chassisId = "chassis_aspect"
    var engineId = "engine_antimatter"
    var subsystemIds = HashMap<Int, String?>()

    @Transient private var chassis: BaseFighterChassis? = null
    @Transient private var engine: BaseFighterEngine? = null
    @Transient private var subsystems: HashMap<Int, BaseFighterSubsystem?>? = null

    //Slot / WeaponSpecId
    var fittedWeapons = HashMap<String, String>()
    var lastStatsObject: FighterStatsObject = FighterStatsObject()


    init {
        subsystemIds[0] = "debug_subsystem"
        subsystemIds[1] = null
        subsystemIds[2] = null
        subsystemIds[3] = null
        subsystemIds[4] = null


        //DEBUG
        //fittedWeapons.set("WS 000", "minipulser")
    }

    fun getChassis() : BaseFighterChassis {
        if (chassis == null) {
            setChassis(chassisId)
        }
        return chassis!!
    }

    fun setChassis(id: String) {
        chassisId = id
        var spec = ComponentPluginLoader.getSpec(id)
        chassis = Global.getSettings().scriptClassLoader.loadClass(spec!!.pluginPath).newInstance() as BaseFighterChassis
        chassis!!.spec = spec
    }

    fun setChassisAndClear(id: String) {
        setChassis(id)

        subsystemIds.clear()
        subsystemIds[0] = null
        subsystemIds[1] = null
        subsystemIds[2] = null
        subsystemIds[3] = null
        subsystemIds[4] = null

        fittedWeapons.clear()

        subsystems = null
        initSubsystems()

        applyDataToSpecs()
    }


    fun getEngine() : BaseFighterEngine {
        if (engine == null) {
            setEngine(engineId)
        }
        return engine!!
    }

    fun setEngine(id: String) {
        engineId = id
        var spec = ComponentPluginLoader.getSpec(id)
        engine = Global.getSettings().scriptClassLoader.loadClass(spec!!.pluginPath).newInstance() as BaseFighterEngine
        engine!!.spec = spec!!
    }


    fun setSubsystemInSlot(slot: Int, subsystemId: String?) {
        initSubsystems()

        subsystemIds[slot] = subsystemId
        if (subsystemId == null) subsystems!![slot] = null
        else subsystems!![slot] = Global.getSettings().scriptClassLoader.loadClass(ComponentPluginLoader.getSpec(subsystemId)!!.pluginPath).newInstance() as BaseFighterSubsystem
    }

    fun getSubsystemInSlot(slot: Int) : BaseFighterSubsystem? {
        initSubsystems()
        return subsystems!!.get(slot)
    }

    fun getAllSubsystems() : List<BaseFighterSubsystem> {
        initSubsystems()
        return subsystems!!.values.filterNotNull()
    }


    fun initSubsystems() {
        if (subsystems == null) {
            subsystems = HashMap()
            subsystems!![0] = null
            subsystems!![1] = null
            subsystems!![2] = null
            subsystems!![3] = null
            subsystems!![4] = null

            if (subsystemIds.get(0) != null) subsystems!![0] = Global.getSettings().scriptClassLoader.loadClass(ComponentPluginLoader.getSpec(subsystemIds[0]!!)!!.pluginPath).newInstance() as BaseFighterSubsystem
            if (subsystemIds.get(1) != null) subsystems!![1] = Global.getSettings().scriptClassLoader.loadClass(ComponentPluginLoader.getSpec(subsystemIds[1]!!)!!.pluginPath).newInstance() as BaseFighterSubsystem
            if (subsystemIds.get(2) != null) subsystems!![2] = Global.getSettings().scriptClassLoader.loadClass(ComponentPluginLoader.getSpec(subsystemIds[2]!!)!!.pluginPath).newInstance() as BaseFighterSubsystem
            if (subsystemIds.get(3) != null) subsystems!![3] = Global.getSettings().scriptClassLoader.loadClass(ComponentPluginLoader.getSpec(subsystemIds[3]!!)!!.pluginPath).newInstance() as BaseFighterSubsystem
            if (subsystemIds.get(4) != null) subsystems!![4] = Global.getSettings().scriptClassLoader.loadClass(ComponentPluginLoader.getSpec(subsystemIds[4]!!)!!.pluginPath).newInstance() as BaseFighterSubsystem
        }
    }



    fun getSpec() = Global.getSettings().getHullSpec(fighterSpecId)
    fun getWingSpec() = Global.getSettings().getFighterWingSpec(fighterWingSpecId)



    fun applyDataToSpecs() {
        var chassis = getChassis()
        var engine = getEngine()
        var subsystems = getAllSubsystems()

        var stats = FighterStatsObject()
        lastStatsObject = stats

        chassis.applyStats(stats)
        engine.applyStats(stats)
        subsystems.forEach { subsystem -> subsystem.applyStats(stats) }


        var fighterSpec = getSpec()
        var wingSpec = Global.getSettings().getFighterWingSpec(fighterWingSpecId)
        var variant = Global.getSettings().getVariant(variantId)

        //Ship Spec
        applyChassisDataToSpec(fighterSpec, chassis)
        fighterSpec.manufacturer = "Modular Design"
        fighterSpec.hullName = name

        fighterSpec.engineSpec.maxSpeed = stats.topSpeed.modifiedValue * stats.speedMult.modifiedValue
        fighterSpec.engineSpec.acceleration = stats.acceleration.modifiedValue * stats.speedMult.modifiedValue
        fighterSpec.engineSpec.deceleration = stats.deceleration.modifiedValue * stats.speedMult.modifiedValue
        fighterSpec.engineSpec.maxTurnRate = stats.maxTurnRate.modifiedValue * stats.speedMult.modifiedValue
        fighterSpec.engineSpec.turnAcceleration = stats.turnAcceleration.modifiedValue * stats.speedMult.modifiedValue

        fighterSpec.setHitpoints(stats.hitpoints.modifiedValue)
        fighterSpec.setArmorRating(stats.armor.modifiedValue)

        //Wing Data
        wingSpec.tags.clear() //Clear Tags just in case

        wingSpec.numFighters = stats.numFighters.modifiedValue.toInt()
        wingSpec.refitTime = stats.refitTime.modifiedValue

        wingSpec.formation = stats.formation
        wingSpec.range = stats.engagementRange.modifiedValue
        //wingSpec.attackRunRange = stats.attackRunRange.modifiedValue
        wingSpec.baseValue = stats.baseValue.modifiedValue

        var op = 0f
        //Ordnance Cost from weapons
        for (weapon in fittedWeapons) {
            var spec = Global.getSettings().getWeaponSpec(weapon.value)
            op += spec.getOrdnancePointCost(Global.getFactory().createPerson().stats)
        }
        op += stats.opCost.modifiedValue
        wingSpec.setOpCost(op)


        //Calc Attack run Range
        var highestRange = 100f
        for (weapon in fittedWeapons) {
            var spec = Global.getSettings().getWeaponSpec(weapon.value)
            if (spec.maxRange >= highestRange) {
                highestRange = spec.maxRange
            }
        }
        highestRange += 50f
        highestRange = MathUtils.clamp(highestRange, 200f, 2000f)
        if (stats.attackAtAngle) highestRange + 50f
        wingSpec.attackRunRange = highestRange

        //Variant Data
        for (slotId in variant.fittedWeaponSlots) {
            variant.clearSlot(slotId)
        }
        variant.weaponGroups.clear()
        for ((slotId, specId) in fittedWeapons) {
            variant.addWeapon(slotId, specId)
            var g = WeaponGroupSpec(WeaponGroupType.LINKED)
            g.addSlot(slotId)
            variant.addWeaponGroup(g)
        }

        //Designation
        wingSpec.role = WingRole.FIGHTER
        variant.setVariantDisplayName("Fighter")
        wingSpec.roleDesc = "Fighter"

        if (stats.role == WingRole.BOMBER) {
            wingSpec.role = WingRole.BOMBER
            wingSpec.roleDesc = "Bomber"
            variant.setVariantDisplayName("Bomber")
        }

        if (stats.role == WingRole.INTERCEPTOR) {
            wingSpec.role = WingRole.INTERCEPTOR
            wingSpec.roleDesc = "Interceptor"
            variant.setVariantDisplayName("Interceptor")
        }

        if (stats.role == WingRole.SUPPORT) {
            wingSpec.role = WingRole.SUPPORT
            wingSpec.roleDesc = "Support"
            variant.setVariantDisplayName("Support")
        }

        if (stats.roleDesc != "") {
            wingSpec.roleDesc = stats.roleDesc
        }

        var test = Global.getSettings().getVariant(chassis.getChassisSpecId() +  "_Hull")

        //Hullmods
        fighterSpec.builtInMods.clear()
        variant.clearHullMods()
        variant.clearPermaMods()

        fighterSpec.addBuiltInMod("modular_fighters_hullmod")
        variant.addPermaMod("modular_fighters_hullmod")

        fighterSpec.addBuiltInMod("no_weapon_flux")
        variant.addPermaMod("no_weapon_flux")

        chassis.getChassisSpec().builtInMods.forEach {
            fighterSpec.addBuiltInMod(it)
            variant.addPermaMod(it)
        }



        fighterSpec.builtInWeapons.clear()
        for (builtin in chassis.getChassisSpec().builtInWeapons) {
            fighterSpec.addBuiltInWeapon(builtin.key, builtin.value)
            variant.addWeapon(builtin.key, builtin.value)
            var spec = Global.getSettings().getWeaponSpec(builtin.value)
            ReflectionUtils.invokeByParameterCount(3,"addWeapon", variant, builtin.key, spec, true)
        }
    }

    //Mounts, Engines, Hullstyle, Sprite, Bounds, Shields, Flux Stats, Hullmods
    //Bounds seem to be handled by the sprite spec, colission radius however is just set high by default in the spec.
    fun applyChassisDataToSpec(spec: ShipHullSpecAPI, chassis: BaseFighterChassis) {
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

        //Flux
        ReflectionUtils.invoke("setReactorSpec", spec, ReflectionUtils.invoke("getReactorSpec", chassisSpec))



    }

}
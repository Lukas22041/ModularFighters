package modular_fighters.components

import com.fs.starfarer.api.Global
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.components.chassis.DebugChassis
import modular_fighters.components.engines.BaseFighterEngine
import modular_fighters.components.engines.DebugEngine
import modular_fighters.components.subsystems.BaseFighterSubsystem
import modular_fighters.modifier.FighterStatsObject

class ModularFighterData(var fighterSpecId: String, var fighterWingSpecId: String, var variantId: String, var name: String) {

    var chassisId = "debug_chassis"
    var engineId = "debug_engine"
    var subsystemIds = HashMap<Int, String?>()

    @Transient private var chassis: BaseFighterChassis? = DebugChassis()
    @Transient private var engine: BaseFighterEngine? = DebugEngine()
    @Transient private var subsystems: HashMap<Int, BaseFighterSubsystem?>? = null

    //Slot / WeaponSpecId
    var fittedWeapons = HashMap<String, String>()

    init {
        subsystemIds[0] = "debug_subsystem"
        subsystemIds[1] = null
        subsystemIds[2] = null
        subsystemIds[3] = null
        subsystemIds[4] = null


        //DEBUG
        fittedWeapons.set("WS 000", "minipulser")
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

        subsystems = null
        initSubsystems()
    }


    fun getEngine() : BaseFighterChassis {
        if (chassis == null) {
            setEngine(chassisId)
        }
        return chassis!!
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

        chassis.applyStats(stats)
        engine.applyStats(stats)
        subsystems.forEach { subsystem -> subsystem.applyStats(stats) }


        var fighterSpec = getSpec()
        var wingSpec = Global.getSettings().getFighterWingSpec(fighterWingSpecId)
        var variantSpec = Global.getSettings().getVariant(variantId)

    }

}
package modular_fighters.components

import com.fs.starfarer.api.Global
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.components.engines.BaseFighterEngine
import modular_fighters.components.subsystems.BaseFighterSubsystem

object ComponentPluginLoader {

    data class ComponentSpec(var id: String, var pluginPath: String)
    private var components = HashMap<String, ComponentSpec>()

    fun loadComponentsFromCSV()
    {
        var CSV = Global.getSettings().getMergedSpreadsheetDataForMod("id", "data/config/modular_fighters_components.csv", "modular_fighters")

        for (index in 0 until  CSV.length())
        {
            val row = CSV.getJSONObject(index)

            val id = row.getString("id")
            val pluginPath = row.getString("plugin")

            var spec = ComponentSpec(id, pluginPath)
            components.put(id, spec)
        }
    }

    fun getSpec(id: String) = components.get(id)

    fun getAllComponents() : List<BaseFighterComponent> {
        return components.map { (Global.getSettings().scriptClassLoader.loadClass(it.value.pluginPath).newInstance() as BaseFighterComponent).apply { this.spec = it.value  } }
    }

    fun getAllChassisComponents() : List<BaseFighterChassis> {
        return getAllComponents().filter { it is BaseFighterChassis } as List<BaseFighterChassis>
    }

    fun getAllEngineComponents() : List<BaseFighterEngine> {
        return getAllComponents().filter { it is BaseFighterEngine } as List<BaseFighterEngine>
    }

    fun getAllSubsystemComponents() : List<BaseFighterSubsystem> {
        return getAllComponents().filter { it is BaseFighterSubsystem } as List<BaseFighterSubsystem>
    }

}
package modular_fighters

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.ShieldAPI
import lunalib.lunaRefit.LunaRefitManager
import modular_fighters.components.chassis.DebugChassis
import modular_fighters.misc.ModularFightersRefitButton
import modular_fighters.misc.ReflectionUtils
import modular_fighters.modifier.SpecModifier

class ModularFightersModPlugin : BaseModPlugin() {

    override fun onApplicationLoad() {
        LunaRefitManager.addRefitButton(ModularFightersRefitButton())
    }

    override fun onGameLoad(newGame: Boolean) {

        ModularFighterUtils.updateSpecsToMatchData()





       /* var spec = Global.getSettings().getHullSpec("modularFighterSpec1")
        var chassis = DebugChassis()
        SpecModifier.applyChassisToSpec(spec, chassis)

        spec = Global.getSettings().getHullSpec("modularFighterSpec2")
        chassis = DebugChassis()
        SpecModifier.applyChassisToSpec(spec, chassis)*/

      /*  var spec = Global.getSettings().getHullSpec("broadsword")
        spec.setHitpoints(200000f)
        spec.setArmorRating(100000f)
        spec.setShieldType(ShieldAPI.ShieldType.NONE)
        //spec.setSpriteName("graphics/ships/fighters/xyphos_hightech.png")

        //Engine Spec
        var engineSpec = spec.engineSpec
        //engineSpec.maxSpeed = 1000f


        var wingSpec = Global.getSettings().getFighterWingSpec("broadsword_wing")
      *//*  wingSpec.tags.clear()
        wingSpec.addTag("auto_fighter")
        wingSpec.addTag("no_drop")
        wingSpec.addTag("no_sell")
        wingSpec.addTag("rapid_reform")
        wingSpec.addTag("leader_no_swarm")
        wingSpec.addTag("wingmen_no_swarm")
        wingSpec.addTag("match_leader_facing")
        wingSpec.addTag("attack_at_an_angle")
        wingSpec.addTag("independent_of_carrier")
        wingSpec.addTag("restricted")*//*
        wingSpec.refitTime = 22f


        var aspectWingSpec = Global.getSettings().getFighterWingSpec("aspect_shock_wing")
        var aspectVariant = Global.getSettings().getVariant("aspect_Shieldbreaker")
        var aspectSpec = Global.getSettings().getHullSpec("aspect")
        var test = ""
*/
    }

}
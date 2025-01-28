package modular_fighters.hullmods

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.loading.WingRole
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import modular_fighters.ModularFighterUtils
import modular_fighters.misc.ReflectionUtils
import modular_fighters.misc.baseOrModSpec
import org.lwjgl.util.vector.Vector2f

class ModularFighterHullmod : BaseHullMod() {

    override fun shouldAddDescriptionToTooltip(hullSize: ShipAPI.HullSize?, ship: ShipAPI?,isForModSpec: Boolean): Boolean {
        return false
    }

    override fun addPostDescriptionSection(tooltip: TooltipMakerAPI?, hullSize: ShipAPI.HullSize?, ship: ShipAPI?, width: Float, isForModSpec: Boolean) {
        tooltip!!.addSpacer(10f)
        tooltip!!.addPara("A fighter that has been designed from a set of unique components.", 0f)
    }

    override fun applyEffectsBeforeShipCreation(hullSize: ShipAPI.HullSize?, stats: MutableShipStatsAPI?, id: String?) {
        var spec = stats?.fleetMember?.baseOrModSpec() ?: return
        var data = ModularFighterUtils.getData().fighterData.get(spec.baseHullId) ?: return
        var wingSpec = Global.getSettings().getFighterWingSpec(data.fighterWingSpecId) ?: return

        if (data.lastStatsObject.attackAtAngle) {
            wingSpec.addTag("rapid_reform")
            wingSpec.addTag("leader_no_swarm")
            wingSpec.addTag("wingmen_no_swarm")
            wingSpec.addTag("match_leader_facing")
            wingSpec.addTag("attack_at_an_angle")
            wingSpec.addTag("independent_of_carrier")
        }

        if (data.lastStatsObject.role == WingRole.BOMBER) {
            wingSpec.tags.add("bomber")
        }

        stats.ballisticWeaponDamageMult.modifyMult(id, data.lastStatsObject.damageMult.modifiedValue)
        stats.energyWeaponDamageMult.modifyMult(id, data.lastStatsObject.damageMult.modifiedValue)
        stats.missileWeaponDamageMult.modifyMult(id, data.lastStatsObject.damageMult.modifiedValue)

        stats.ballisticWeaponRangeBonus.modifyMult(id, data.lastStatsObject.rangeMult.modifiedValue)
        stats.energyWeaponRangeBonus.modifyMult(id, data.lastStatsObject.rangeMult.modifiedValue)
        stats.missileWeaponRangeBonus.modifyMult(id, data.lastStatsObject.rangeMult.modifiedValue)

       /* stats.ballisticRoFMult.modifyMult(id, data.lastStatsObject.fireRateMult.modifiedValue)
        stats.energyRoFMult.modifyMult(id, data.lastStatsObject.fireRateMult.modifiedValue)
        stats.missileRoFMult.modifyMult(id, data.lastStatsObject.fireRateMult.modifiedValue)

        stats.ballisticAmmoRegenMult.modifyMult(id, data.lastStatsObject.fireRateMult.modifiedValue)
        stats.energyAmmoRegenMult.modifyMult(id, data.lastStatsObject.fireRateMult.modifiedValue)
        stats.missileAmmoRegenMult.modifyMult(id, data.lastStatsObject.fireRateMult.modifiedValue)*/

        stats.weaponTurnRateBonus.modifyFlat(id, 10f)
        stats.weaponTurnRateBonus.modifyMult(id, data.lastStatsObject.weaponTurnRate.modifiedValue)
        stats.autofireAimAccuracy.modifyFlat(id, data.lastStatsObject.autoFireAim.modifiedValue)
    }

    override fun applyEffectsAfterShipCreation(ship: ShipAPI?, id: String?) {
        var spec = ship?.baseOrModSpec() ?: return
        var data = ModularFighterUtils.getData().fighterData.get(spec.baseHullId) ?: return
        var wingSpec = Global.getSettings().getFighterWingSpec(data.fighterWingSpecId) ?: return

        if (data.lastStatsObject.isIndependent) {

            if (ship.isWingLeader) {
                Global.getCombatEngine()?.addPlugin(FighterIndependenceHmodScript(ship!!, data.lastStatsObject.isIndependentNoReturn))
            }

        }

        for (weapon in ship.allWeapons) {
            var spec = weapon.spec

            if (spec.type == WeaponAPI.WeaponType.MISSILE || spec.type == WeaponAPI.WeaponType.SYNERGY || spec.type == WeaponAPI.WeaponType.COMPOSITE) {

            }
            else if (spec.maxAmmo > 0 && spec.maxAmmo != Int.MAX_VALUE && spec.ammoPerSecond == 0f) {
                weapon.maxAmmo = weapon.spec.maxAmmo / 10
                weapon.ammo = weapon.maxAmmo
            }
        }

        /*var chassisSpec = data.getChassis().getChassisSpec()
        for (weapon in ship.allWeapons) {

           // weapon.slot.location.set(Vector2f())
            weapon.slot
            weapon.ensureClonedSpec()
            weapon.sprite.setSize(weapon.sprite.width / 2f, weapon.sprite.width / 2f)

            var originalSlot = chassisSpec.getWeaponSlot(weapon.slot.id)

            for (slot in ship.hullSpec.allWeaponSlotsCopy) {
                slot.location.set(Vector2f(originalSlot.location.x / 2, originalSlot.location.y / 2))
            }
        }*/

    }

    override fun advanceInCombat(ship: ShipAPI?, amount: Float) {
       /* ReflectionUtils.set("isAnimatedLaunch", ship!!, true)
        ReflectionUtils.set("sinceLaunch", ship!!, 0.5f)*/
    }

}


class FighterIndependenceHmodScript(var ship: ShipAPI, var noReturn: Boolean) : BaseEveryFrameCombatPlugin() {

    var source: FighterLaunchBayAPI? = null
    var initated = false

    override fun advance(amount: Float, events: MutableList<InputEventAPI>?) {
        var wing = ship.wing
        if (wing != null && !initated && wing.wingMembers.size == wing.spec.numFighters) {
            initated = true
            source = wing.source
            wing.setSourceBay(null)
        }

        if (!ship.isAlive && source != null && wing != null && !noReturn) {
            wing.setSourceBay(source)
            Global.getCombatEngine().removePlugin(this)
        }
    }
}
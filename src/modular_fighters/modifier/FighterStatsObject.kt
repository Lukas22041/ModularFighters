package modular_fighters.modifier

import com.fs.starfarer.api.combat.MutableStat
import com.fs.starfarer.api.loading.FormationType

class FighterStatsObject {

    var numFighters = MutableStat(0f)
    var refitTime = MutableStat(0f)
    var crewPerFighter = MutableStat(0f)

    var topSpeed = MutableStat(0f)
    var acceleration = MutableStat(0f)
    var deceleration = MutableStat(0f)
    var maxTurnRate = MutableStat(0f)
    var turnAcceleration = MutableStat(0f)

    var hitpoints = MutableStat(0f)
    var armor = MutableStat(0f)

    var formation: FormationType = FormationType.V
    var engagementRange = MutableStat(0f)
    var attackRunRange = MutableStat(0f)
    var baseValue = MutableStat(1000f)

    var damageMult = MutableStat(1f)
    var rangeMult = MutableStat(1f)
    var fireRateMult = MutableStat(1f)

    var isIndependent = false
    var attackAtAngle = false

}
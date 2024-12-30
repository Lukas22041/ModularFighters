package modular_fighters.modifier

import com.fs.starfarer.api.combat.MutableStat

class FighterStatsObject {

    var topSpeed = MutableStat(0f)
    var acceleration = MutableStat(0f)
    var decceleration = MutableStat(0f)
    var turnAcceleration = MutableStat(0f)
    var maxTurnSpeed = MutableStat(0f)

    var isIndependent = false
    var attackAtAngle = false

}
package modular_fighters.modifier

import com.fs.starfarer.api.combat.MutableStat
import com.fs.starfarer.api.loading.FormationType
import com.fs.starfarer.combat.ai.FighterWingFormation

class FighterStatsObject {

    var numFighters = MutableStat(0f)

    var topSpeed = MutableStat(0f)
    var acceleration = MutableStat(0f)
    var deceleration = MutableStat(0f)
    var turnAcceleration = MutableStat(0f)
    var maxTurnSpeed = MutableStat(0f)

    var isIndependent = false
    var attackAtAngle = false

    var hitpoints = MutableStat(0f)
    var armor = MutableStat(0f)

    var formation: FormationType = FormationType.V
    var engagementRange = MutableStat(0f)
    var attackRunRange = MutableStat(0f)
    var baseValue = MutableStat(1000f)


}
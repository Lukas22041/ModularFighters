package modular_fighters.hullmods

import com.fs.starfarer.api.EveryFrameScript
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.util.IntervalUtil

class HullmodAdderScript : EveryFrameScript {

    var interval = IntervalUtil(1f, 2f)

    override fun isDone(): Boolean {
        return false
    }

    override fun runWhilePaused(): Boolean {
        return true
    }

    override fun advance(amount: Float) {
        var members = Global.getSector().playerFleet.fleetData.membersListCopy

        interval.advance(amount)
        if (interval.intervalElapsed()) {
            for (member in members) {
                member.variant.addPermaMod("modular_fighters_manager")
            }
        }
    }

}
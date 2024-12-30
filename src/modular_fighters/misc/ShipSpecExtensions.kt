package modular_fighters.misc

import com.fs.starfarer.api.combat.ShieldAPI.ShieldType
import com.fs.starfarer.api.combat.ShipHullSpecAPI
import modular_fighters.misc.ReflectionUtils

//Convoluted Solution to the fact that field indexes may not be static
fun ShipHullSpecAPI.setArmorRating(rating: Float) {
    var armorSpec = ReflectionUtils.invoke("getArmorSpec", this)

    var checkNumber = 336699f

    var default0 = ReflectionUtils.getAtIndex(0, armorSpec!!)

    ReflectionUtils.setAtIndex(0, armorSpec, checkNumber)
    if (this.armorRating == checkNumber) {
        //Found the right variable immediately, just change it to the new rating, then return
        ReflectionUtils.setAtIndex(0, armorSpec, rating)
        return
    }
    //First variable was not the right value, as such set the other variable and undo the check
    ReflectionUtils.setAtIndex(0, armorSpec, default0)
    ReflectionUtils.setAtIndex(1, armorSpec, rating)
}

//Convoluted Solution to the fact that field indexes may not be static
fun ShipHullSpecAPI.setHitpoints(hitpoints: Float) {
    var armorSpec = ReflectionUtils.invoke("getArmorSpec", this)

    var checkNumber = 336699f

    var default0 = ReflectionUtils.getAtIndex(0, armorSpec!!)

    ReflectionUtils.setAtIndex(0, armorSpec, checkNumber)
    if (this.hitpoints == checkNumber) {
        //Found the right variable immediately, just change it to the new rating, then return
        ReflectionUtils.setAtIndex(0, armorSpec, hitpoints)
        return
    }
    //First variable was not the right value, as such set the other variable and undo the check
    ReflectionUtils.setAtIndex(0, armorSpec, default0)
    ReflectionUtils.setAtIndex(1, armorSpec, hitpoints)
}

fun ShipHullSpecAPI.setShieldType(type: ShieldType) {
    var shieldSpec = ReflectionUtils.invoke("getShieldSpec", this)
    ReflectionUtils.invoke("setType", shieldSpec!!, type)
}


//Doesnt really work, check the docs for a reason why and a solution
/*
fun ShipHullSpecAPI.setSpriteName(spriteName: String) {

    //var sprite = Global.getSettings().getSprite("graphics/ships/fighters/xyphos_hightech.png")

    var spriteSpec = ReflectionUtils.invoke("getSpriteSpec", this)

    var test = ""

    ReflectionUtils.setFieldOfType(String::class.java, spriteSpec!!, spriteName)
    //ReflectionUtils.setFieldOfType(Float::class.java, spriteSpec, 20f)
}*/

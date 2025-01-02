package modular_fighters.ui.elements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.loading.WeaponSpecAPI
import com.fs.starfarer.api.ui.Fonts
import com.fs.starfarer.api.ui.LabelAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaUI.elements.LunaElement
import modular_fighters.components.ModularFighterData
import org.lazywizard.lazylib.MathUtils
import org.lwjgl.input.Keyboard
import kotlin.math.min

class WeaponListElement(var mount: WeaponMountElement, var spec: WeaponSpecAPI, quanity: Int, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    var fade = 0f


    init {
        WeaponDisplayElement(mount.mount, spec, 1f, innerElement, 20f, 20f).apply {
            position.inTL(20f, height/2-10f)
        }

        var anchor1 = innerElement.addLunaElement(0f, 0f)
        anchor1.position.inTL(65f, 5f)

        var namePara = innerElement.addPara(spec.weaponName, 0f)
        namePara.position.rightOfTop(anchor1.elementPanel, 0f)

        var anchor2 = innerElement.addLunaElement(0f, 0f)
        anchor2.position.belowLeft(anchor1.elementPanel, namePara.computeTextHeight(namePara.text) + 2f)

        var rolePara = innerElement.addPara("${spec.primaryRoleStr}, range ${spec.maxRange.toInt()}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        rolePara.position.rightOfTop(anchor2.elementPanel, 0f)

        var anchor3 = innerElement.addLunaElement(0f, 0f)
        anchor3.position.belowLeft(anchor2.elementPanel, rolePara.computeTextHeight(rolePara.text) + 2f)

        var quantityPara = innerElement.addPara("$quanity available", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "$quanity")
        quantityPara.position.rightOfTop(anchor3.elementPanel, 0f)

        var anchor4 = innerElement.addLunaElement(0f, 0f)
        anchor4.position.belowLeft(anchor3.elementPanel, quantityPara.computeTextHeight(quantityPara.text) + 2f)

        innerElement.setParaFont(Fonts.VICTOR_10)
        var cargoPara = innerElement.addPara("In your cargo holds", 0f, Misc.getGrayColor(), Misc.getGrayColor())
        cargoPara.position.rightOfTop(anchor4.elementPanel, 0f)

        var anchor5 = innerElement.addLunaElement(0f, 0f)
        var ordnanceTextPara = innerElement.addPara("Ordnance Points", 0f, Misc.getGrayColor(), Misc.getGrayColor())

        anchor5.position.rightOfTop(anchor4.elementPanel, width-65f-ordnanceTextPara.computeTextWidth(ordnanceTextPara.text)-10f )

        ordnanceTextPara.position.rightOfTop(anchor5.elementPanel, 0f)

        innerElement.setParaFont(Fonts.ORBITRON_24AABOLD)


        var anchor6 = innerElement.addLunaElement(0f, 0f)
        var ordnanceNumberPara = innerElement.addPara("${spec.getOrdnancePointCost(Global.getFactory().createPerson().stats).toInt()}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())

        anchor6.position.rightOfTop(anchor4.elementPanel, width-65f-ordnanceNumberPara.computeTextWidth(ordnanceNumberPara.text)-10f )
        ordnanceNumberPara.position.aboveLeft(anchor6.elementPanel, 0f)

        /*onHover {
            it.consume()
        }*/

        /*onHoverEnter {
            it.consume()
        }
        onHoverExit {
            it.consume()
        }*/

    }

    override fun advance(amount: Float) {
        if (isHovering) fade += 20 * amount
        else fade -= 5 * amount
        fade = MathUtils.clamp(fade, 0.0f, 1f)

        backgroundAlpha = fade
    }

    override fun render(alphaMult: Float) {


    }



}
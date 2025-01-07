package modular_fighters.ui.elements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.graphics.SpriteAPI
import com.fs.starfarer.api.ui.Fonts
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaUI.elements.LunaElement
import modular_fighters.components.BaseFighterComponent
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.modifier.FighterStatsObject
import org.lazywizard.lazylib.MathUtils
import kotlin.math.min

class ComponentsListElement(var stats: FighterStatsObject, var component: BaseFighterComponent, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    var fade = 0f

    var sprite: SpriteAPI? = null
    var baseW = 0f
    var baseH = 0f

    init {
        /*WeaponDisplayElement(mount.mount, spec, 1f, innerElement, 20f, 20f).apply {
            position.inTL(20f, height/2-10f)
        }*/

        if (component is BaseFighterChassis) {
            sprite = Global.getSettings().getSprite((component as BaseFighterChassis).getChassisSpec().spriteName)
        }

        if (sprite != null) {
            baseW = sprite!!.width
            baseH = sprite!!.height
        }

        var anchor1 = innerElement.addLunaElement(0f, 0f)
        anchor1.position.inTL(65f, 5f)

        var namePara = innerElement.addPara(component.getName(), 0f)
        namePara.position.rightOfTop(anchor1.elementPanel, 0f)

        var anchor2 = innerElement.addLunaElement(0f, 0f)
        anchor2.position.belowLeft(anchor1.elementPanel, namePara.computeTextHeight(namePara.text) + 2f)

        var designTypePara = innerElement.addPara("Design type: ${component.getDesignType()}", 0f, Misc.getGrayColor(), Misc.getDesignTypeColor(component.getDesignType()), "${component.getDesignType()}")
        designTypePara.position.rightOfTop(anchor2.elementPanel, 0f)

        var anchor3 = innerElement.addLunaElement(0f, 0f)
        anchor3.position.belowLeft(anchor2.elementPanel, designTypePara.computeTextHeight(designTypePara.text) + 2f)

        var midPara = innerElement.addPara("", 0f)
        if (component is BaseFighterChassis) {
            midPara = innerElement.addPara("${stats.role.name.lowercase().capitalize()} Chassis", 0f, Misc.getHighlightColor(), Misc.getHighlightColor(), "")
            midPara.position.rightOfTop(anchor3.elementPanel, 0f)
        }

        var anchor4 = innerElement.addLunaElement(0f, 0f)
        anchor4.position.belowLeft(anchor3.elementPanel, midPara.computeTextHeight(midPara.text) + 2f)

        innerElement.setParaFont(Fonts.VICTOR_10)
        var cargoPara = innerElement.addPara("Blueprint Available", 0f, Misc.getGrayColor(), Misc.getGrayColor())
        cargoPara.position.rightOfTop(anchor4.elementPanel, 0f)

        var anchor5 = innerElement.addLunaElement(0f, 0f)
        var ordnanceTextPara = innerElement.addPara("Ordnance Points", 0f, Misc.getGrayColor(), Misc.getGrayColor())

        anchor5.position.rightOfTop(anchor4.elementPanel, width-65f-ordnanceTextPara.computeTextWidth(ordnanceTextPara.text)-10f )

        ordnanceTextPara.position.rightOfTop(anchor5.elementPanel, 0f)

        innerElement.setParaFont(Fonts.ORBITRON_24AABOLD)


        var anchor6 = innerElement.addLunaElement(0f, 0f)
        var ordnanceNumberPara = innerElement.addPara("${stats.opCost.modifiedInt}", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())

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
        super.render(alphaMult)

        if (sprite == null) return

        var maxW = 40f
        var maxH = height * 0.85f

        var scale = min(maxW / baseW, maxH / baseH)
        sprite!!.setSize(baseW * scale, baseH * scale)
        sprite!!.renderAtCenter(x + 30f, y + height / 2 - 1)

    }



}
package modular_fighters.ui.elements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.ui.Fonts
import com.fs.starfarer.api.ui.TooltipMakerAPI
import lunalib.lunaUI.elements.LunaElement
import modular_fighters.components.ModularFighterData
import kotlin.math.min

class FighterListElement(var data: ModularFighterData, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    var sprite = Global.getSettings().getSprite(data.chassis.getChassisSpec().spriteName)
    var backgroundSprite = Global.getSettings().getSprite("graphics/icons/cargo/blueprint_fighter.png")
    var baseW = sprite.width
    var baseH = sprite.height

    init {
        enableTransparency = true
        renderBorder = false
        renderBackground = true
        backgroundAlpha = 0f

        innerElement.setParaFont("graphics/fonts/victor14.fnt")
        var namePara = innerElement.addPara("${data.name}", 0f)
        namePara.position.inTL(60f, height / 2 - namePara.computeTextHeight(namePara.text) / 2)

        onClick {
            playClickSound()
        }

        onHoverEnter {
            playScrollSound()
            //backgroundAlpha = 0.3f
        }
        //onHoverExit { backgroundAlpha = 0f }
    }

    override fun render(alphaMult: Float) {
        super.render(alphaMult)

        var containerWidth = 40f
        var containerHeight = 40f

        backgroundSprite.setSize(containerWidth, containerHeight)
        backgroundSprite.alphaMult = 0.9f
        backgroundSprite.renderAtCenter(x + 25f, y + height / 2 + 2)

        var maxW = 15f
        var maxH = 15f

        var scale = min(maxW / baseW, maxH / baseH)

        sprite.setSize(baseW * scale, baseH * scale)
        sprite.alphaMult = 0.9f
        sprite.render(x + 21f, y + height / 2 - 4)

    }

}
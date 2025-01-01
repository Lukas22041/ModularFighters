package modular_fighters.ui.elements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.graphics.SpriteAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import lunalib.lunaUI.elements.LunaElement
import modular_fighters.components.chassis.BaseFighterChassis
import org.dark.shaders.util.ShaderLib
import org.lazywizard.lazylib.MathUtils
import org.lwjgl.opengl.GL20

class ChassisDisplayElement(var chassis: BaseFighterChassis, var sprite: SpriteAPI, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    companion object {
        var shader = 0
    }

    var fade = 0f

    init {
        enableTransparency = true
        renderBorder = false
        renderBackground = false

        if (shader == 0) {
            shader = ShaderLib.loadShader(Global.getSettings().loadText("data/shaders/baseVertex.shader"),
                Global.getSettings().loadText("data/shaders/outlineFragment.shader"))
            if (shader != 0) {
                GL20.glUseProgram(shader)
                GL20.glUniform1i(GL20.glGetUniformLocation(shader, "tex"), 0)
                GL20.glUseProgram(0)
            } else {
                var test = ""
            }
        }
    }

    override fun advance(amount: Float) {
        super.advance(amount)

        if (isHovering) fade += 5 * amount
        else fade -= 5 * amount
        fade = MathUtils.clamp(fade, 0f, 1f)
    }


    override fun render(alphaMult: Float) {
        super.render(alphaMult)

        sprite.setSize(width, height)
        sprite.alphaMult = 0.95f
        sprite.render(x, y)


        GL20.glUseProgram(shader)

        var col1 = chassis.getOutlineColor1()
        var col2 = chassis.getOutlineColor2()

        GL20.glUniform1f(GL20.glGetUniformLocation(shader, "alphaMult"), alphaMult * fade)
        GL20.glUniform3f(GL20.glGetUniformLocation(shader, "outlineCol1"), col1.red / 255f, col1.green / 255f, col1.blue / 255f)
        GL20.glUniform3f(GL20.glGetUniformLocation(shader, "outlineCol2"), col2.red / 255f, col2.green / 255f, col2.blue / 255f)

        sprite.setSize(width + 4, height + 4f)
        sprite.alphaMult = 0.95f
        sprite.render(x - 2, y - 2)

        GL20.glUseProgram(0)


    }

}
package modular_fighters.ui.elements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.graphics.SpriteAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import lunalib.lunaUI.elements.LunaElement
import modular_fighters.components.chassis.BaseFighterChassis
import org.dark.shaders.util.ShaderLib
import org.lwjgl.opengl.GL20

class ChassisElement(var chassis: BaseFighterChassis, var sprite: SpriteAPI, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    companion object {
        var shader = 0
    }


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



    override fun render(alphaMult: Float) {
        super.render(alphaMult)

        sprite.setSize(width, height)
        sprite.alphaMult = 0.95f
        sprite.render(x, y)

        if (isHovering) {
            GL20.glUseProgram(shader)

            GL20.glUniform1f(GL20.glGetUniformLocation(shader, "alphaMult"), alphaMult)

            sprite.setSize(width + 4, height + 4f)
            sprite.alphaMult = 0.95f
            sprite.render(x - 2, y - 2)

            GL20.glUseProgram(0)
        }


    }

}
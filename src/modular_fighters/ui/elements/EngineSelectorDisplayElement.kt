package modular_fighters.ui.elements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.graphics.SpriteAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import lunalib.lunaUI.elements.LunaElement
import modular_fighters.components.engines.BaseFighterEngine
import modular_fighters.misc.ConstantTimeIncreaseScript
import modular_fighters.misc.getAndLoadSprite
import org.dark.shaders.util.ShaderLib
import org.lazywizard.lazylib.MathUtils
import org.lwjgl.opengl.GL20
import org.lwjgl.util.vector.Vector3f
import kotlin.math.min

class EngineSelectorDisplayElement(var engine: BaseFighterEngine, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    companion object {
        var shader = 0
    }

    var fade = 0f
    var sprite: SpriteAPI = Global.getSettings().getAndLoadSprite(engine.getEngineIcon())

    init {
        enableTransparency = true
        renderBorder = false
        renderBackground = false

        //if (shader == 0) {
            shader = ShaderLib.loadShader(Global.getSettings().loadText("data/shaders/baseVertex.shader"),
                Global.getSettings().loadText("data/shaders/glitchFragment.shader"))
            if (shader != 0) {
                GL20.glUseProgram(shader)
                GL20.glUniform1i(GL20.glGetUniformLocation(shader, "tex"), 0)
                GL20.glUseProgram(0)
            } else {
                var test = ""
            }
       // }
    }

    override fun advance(amount: Float) {
        super.advance(amount)

        if (isHovering) fade += 5 * amount
        else fade -= 5 * amount
        fade = MathUtils.clamp(fade, 0f, 1f)
    }


    override fun render(alphaMult: Float) {
        super.render(alphaMult)

        GL20.glUseProgram(shader)

        var colorMult = Vector3f(1f + 0.3f * fade, 1f + 0.3f * fade, 0.8f + 0.2f * fade)
        var time = (Global.getSector().scripts.find { it is ConstantTimeIncreaseScript } as ConstantTimeIncreaseScript).time / (8-4*fade)
        GL20.glUniform3f(GL20.glGetUniformLocation(shader, "colorMult"), colorMult.x, colorMult.y, colorMult.z)
        GL20.glUniform1f(GL20.glGetUniformLocation(shader, "iTime"), time)
        GL20.glUniform1f(GL20.glGetUniformLocation(shader, "alphaMult"), 0.9f + 0.1f * fade)

        sprite.setSize(38f, 38f)
        sprite.renderAtCenter(x + width / 2 , y + height / 2 )

        GL20.glUseProgram(0)


    }

}
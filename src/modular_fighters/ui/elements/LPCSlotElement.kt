package modular_fighters.ui.elements

import com.fs.starfarer.api.ui.TooltipMakerAPI
import lunalib.lunaUI.elements.LunaElement
import org.lazywizard.lazylib.MathUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Vector2f
import java.awt.Color
import kotlin.math.abs

class LPCSlotElement(var lineAngle: Float, var lineTarget: Vector2f, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    init {
        enableTransparency = true
        borderAlpha = 0.5f
        backgroundColor = Color(0, 0, 0)
    }


    override fun render(alphaMult: Float) {
        super.render(alphaMult)

        var sideways = false
        var startingPoint = Vector2f()
        if (lineAngle in 45f..135f) startingPoint = Vector2f(x + width / 2, y+height)
        else if (lineAngle in 135f..225f) {
            startingPoint = Vector2f(x, y+height/2)
            sideways = true
        }
        else if (lineAngle in 225f..315f) startingPoint = Vector2f(x + width / 2, y)
        else {
            startingPoint = Vector2f(x+width, y + height/2)
            sideways = true
        }

        var middlePoint = Vector2f(startingPoint)

        if (!sideways) {
            var difference = abs(middlePoint.y - lineTarget.y)
            while (true) {

                middlePoint = MathUtils.getPointOnCircumference(middlePoint, 1f, lineAngle)
                //var newDistance = middlePoint.getDistance(lineTarget)
                var newDifference = abs(middlePoint.y - lineTarget.y)

                if (newDifference >= difference) {
                    break
                }

                difference = newDifference
            }
        }
        else {
            var difference = abs(middlePoint.x - lineTarget.x)
            while (true) {

                middlePoint = MathUtils.getPointOnCircumference(middlePoint, 1f, lineAngle)
                //var newDistance = middlePoint.getDistance(lineTarget)
                var newDifference = abs(middlePoint.x - lineTarget.x)

                if (newDifference >= difference) {
                    break
                }

                difference = newDifference
            }
        }



        var c = borderColor
        GL11.glPushMatrix()

        GL11.glTranslatef(0f, 0f, 0f)
        GL11.glRotatef(0f, 0f, 0f, 1f)

        GL11.glDisable(GL11.GL_TEXTURE_2D)

        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        GL11.glColor4f(c.red / 255f,
            c.green / 255f,
            c.blue / 255f,
            c.alpha / 255f * (alphaMult ))

        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glBegin(GL11.GL_LINE_STRIP)

        GL11.glVertex2f(startingPoint.x, startingPoint.y)
        GL11.glVertex2f(middlePoint.x, middlePoint.y)
        GL11.glVertex2f(lineTarget.x, lineTarget.y)

        GL11.glEnd()
        GL11.glPopMatrix()

    }

}
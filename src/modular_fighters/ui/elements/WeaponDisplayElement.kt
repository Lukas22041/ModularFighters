package modular_fighters.ui.elements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.WeaponAPI
import com.fs.starfarer.api.graphics.SpriteAPI
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.loading.WeaponSlotAPI
import com.fs.starfarer.api.loading.WeaponSpecAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import lunalib.lunaUI.elements.LunaElement
import modular_fighters.components.chassis.BaseFighterChassis
import modular_fighters.ui.LPCDesignerPanel
import org.dark.shaders.util.ShaderLib
import org.lazywizard.lazylib.MathUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL20
import org.lwjgl.util.vector.Vector2f
import java.awt.Color

class WeaponDisplayElement(var mount: WeaponSlotAPI, var weapon: WeaponSpecAPI, var scale: Float, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    companion object {
        var shader = 0
    }

    var weaponSprite: SpriteAPI? = null

    init {
        enableTransparency = true
        renderBorder = false
        renderBackground = false

        if (mount.isTurret && weapon!!.turretSpriteName != null && weapon!!.turretSpriteName != "") {
            weaponSprite = Global.getSettings().getSprite(weapon!!.turretSpriteName)
            weaponSprite!!.setSize(weaponSprite!!.width * scale, weaponSprite!!.height * scale)
        }
        if (mount.isHardpoint && weapon!!.hardpointSpriteName != null && weapon!!.hardpointUnderSpriteName != "") {
            weaponSprite = Global.getSettings().getSprite(weapon!!.hardpointSpriteName)
            weaponSprite!!.setSize(weaponSprite!!.width * scale, weaponSprite!!.height * scale)
        }
    }

    override fun processInput(events: MutableList<InputEventAPI>?) {
        //super.processInput(events)
    }

    override fun advance(amount: Float) {
        super.advance(amount)

    }

    override fun render(alphaMult: Float) {
        super.render(alphaMult)


        var alpha = 0.8f

        var color = when(weapon.mountType) {
            WeaponAPI.WeaponType.BALLISTIC -> Misc.MOUNT_BALLISTIC
            WeaponAPI.WeaponType.ENERGY -> Misc.MOUNT_ENERGY
            WeaponAPI.WeaponType.MISSILE -> Misc.MOUNT_MISSILE
            WeaponAPI.WeaponType.HYBRID -> Misc.MOUNT_HYBRID
            WeaponAPI.WeaponType.COMPOSITE -> Misc.MOUNT_COMPOSITE
            WeaponAPI.WeaponType.SYNERGY -> Misc.MOUNT_SYNERGY
            else -> Misc.MOUNT_UNIVERSAL
        }

        var c = color

        GL11.glLineWidth(3f)

        //Ballistic
        if (weapon.mountType == WeaponAPI.WeaponType.HYBRID || weapon.mountType == WeaponAPI.WeaponType.BALLISTIC || weapon.mountType == WeaponAPI.WeaponType.COMPOSITE || weapon.mountType == WeaponAPI.WeaponType.UNIVERSAL) {
            GL11.glPushMatrix()

            GL11.glTranslatef(0f, 0f, 0f)
            GL11.glRotatef(0f, 0f, 0f, 1f)

            GL11.glDisable(GL11.GL_TEXTURE_2D)

            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            GL11.glColor4f(c.red / 255f,
                c.green / 255f,
                c.blue / 255f,
                c.alpha / 255f * (alphaMult * alpha))

            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glBegin(GL11.GL_LINE_STRIP)

            GL11.glVertex2f(x, y)
            GL11.glVertex2f(x, y + height)
            GL11.glVertex2f(x + width , y + height )
            GL11.glVertex2f(x + width , y)
            GL11.glVertex2f(x, y)

            GL11.glEnd()
            GL11.glPopMatrix()
        }

        //Energy
        if (weapon.mountType == WeaponAPI.WeaponType.ENERGY || weapon.mountType == WeaponAPI.WeaponType.SYNERGY || weapon.mountType == WeaponAPI.WeaponType.HYBRID || weapon.mountType == WeaponAPI.WeaponType.UNIVERSAL) {
            GL11.glPushMatrix()

            GL11.glTranslatef(0f, 0f, 0f)
            GL11.glRotatef(0f, 0f, 0f, 1f)

            GL11.glDisable(GL11.GL_TEXTURE_2D)

            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            GL11.glColor4f(c.red / 255f,
                c.green / 255f,
                c.blue / 255f,
                c.alpha / 255f * (alphaMult * alpha))

            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glBegin(GL11.GL_LINE_STRIP)

            var points = 100
            var arc = 360f
            var arcPer = arc / points
            var progress = 0f

            var extraSize = 5
            var radius = width * 0.75f

            for (i in 0 .. points) {
                var point = MathUtils.getPointOnCircumference(Vector2f(x+width/2, y+height/2), radius, progress)
                progress += arcPer
                GL11.glVertex2f(point.x, point.y)
            }

            GL11.glEnd()
            GL11.glPopMatrix()
        }

        //Missile
        if (weapon.mountType == WeaponAPI.WeaponType.MISSILE || mount.weaponType == WeaponAPI.WeaponType.COMPOSITE || weapon.mountType == WeaponAPI.WeaponType.SYNERGY || weapon.mountType == WeaponAPI.WeaponType.UNIVERSAL) {
            GL11.glPushMatrix()

            GL11.glTranslatef(0f, 0f, 0f)
            GL11.glRotatef(0f, 0f, 0f, 1f)

            GL11.glDisable(GL11.GL_TEXTURE_2D)

            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            GL11.glColor4f(c.red / 255f,
                c.green / 255f,
                c.blue / 255f,
                c.alpha / 255f * (alphaMult * alpha))

            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glBegin(GL11.GL_LINE_STRIP)

            GL11.glVertex2f(x, y + height / 2 )
            GL11.glVertex2f(x + width / 2 , y + height )
            GL11.glVertex2f(x + width, y + height / 2 )
            GL11.glVertex2f(x + width / 2, y)
            GL11.glVertex2f(x, y + height / 2 )

            GL11.glEnd()
            GL11.glPopMatrix()
        }


        GL11.glLineWidth(1f)

        renderWeapon()

    }

    fun renderWeapon() {
        if (weaponSprite == null) return

        weaponSprite!!.angle = -45f
        weaponSprite!!.renderAtCenter(x+width/2, y+height/2)

    }

    fun renderArcLines(angle: Float, c: Color, alpha: Float) {
        GL11.glPushMatrix()

        GL11.glTranslatef(0f, 0f, 0f)
        GL11.glRotatef(0f, 0f, 0f, 1f)

        GL11.glDisable(GL11.GL_TEXTURE_2D)

        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)



        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glBegin(GL11.GL_LINE_STRIP)

        var points = 100
        var maxDistance = width * 4
        var distancePer = maxDistance / points
        var alphaPer = 1f / points * 2f
        var swappedAlpha = false
        var alphaMod = 0f

        var distance = 0f

        for (i in 0 .. points) {
            var point = MathUtils.getPointOnCircumference(Vector2f(x+width/2, y+height/2), distance, angle)
            distance += distancePer

            GL11.glColor4f(c.red / 255f,
                c.green / 255f,
                c.blue / 255f,
                c.alpha / 255f * (alpha * alphaMod * 0.5f))
            GL11.glVertex2f(point.x, point.y)

            if (!swappedAlpha)alphaMod += alphaPer
            else alphaMod -= alphaPer
            if (alphaMod >= 1) swappedAlpha = true

        }

        GL11.glEnd()
        GL11.glPopMatrix()
    }

}
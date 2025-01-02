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

class WeaponMountElement(var designer: LPCDesignerPanel, var mount: WeaponSlotAPI, var weapon: WeaponSpecAPI?, var scale: Float, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    companion object {
        var shader = 0
    }

    var weaponSprite: SpriteAPI? = null

    var fade = 0f

    init {
        enableTransparency = true
        renderBorder = false
        renderBackground = false

        onClick {
            playSound("ui_button_mouseover", 1f, 1f)
        }

        onHoverEnter {
            playScrollSound()
        }

        if (weapon != null) {
            if (mount.isTurret && weapon!!.turretSpriteName != null && weapon!!.turretSpriteName != "") {
                weaponSprite = Global.getSettings().getSprite(weapon!!.turretSpriteName)
                weaponSprite!!.setSize(weaponSprite!!.width * scale, weaponSprite!!.height * scale)
            }
            if (mount.isHardpoint && weapon!!.hardpointSpriteName != null && weapon!!.hardpointUnderSpriteName != "") {
                weaponSprite = Global.getSettings().getSprite(weapon!!.hardpointSpriteName)
                weaponSprite!!.setSize(weaponSprite!!.width * scale, weaponSprite!!.height * scale)
            }
        }
    }

    override fun advance(amount: Float) {
        super.advance(amount)

        if (isHovering || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || designer.selectedMount == this) fade += 10 * amount
        else fade -= 10 * amount
        fade = MathUtils.clamp(fade, 0f, 1f)
    }

    override fun render(alphaMult: Float) {
        super.render(alphaMult)


        var renderBelow = false
        if (fade > 0.25f) renderBelow = true
        //Weapon Render Here if the fade is above 0.25
        if (renderBelow) renderWeapon()


        var size = (width * fade)
        var alpha = 0.5f + (0.5f * fade)

        var color = when(mount.weaponType) {
            WeaponAPI.WeaponType.BALLISTIC -> Misc.MOUNT_BALLISTIC
            WeaponAPI.WeaponType.ENERGY -> Misc.MOUNT_ENERGY
            WeaponAPI.WeaponType.MISSILE -> Misc.MOUNT_MISSILE
            WeaponAPI.WeaponType.HYBRID -> Misc.MOUNT_HYBRID
            WeaponAPI.WeaponType.COMPOSITE -> Misc.MOUNT_COMPOSITE
            WeaponAPI.WeaponType.SYNERGY -> Misc.MOUNT_SYNERGY
            else -> Misc.MOUNT_UNIVERSAL
        }

        var c = color

        GL11.glLineWidth(2f)

        //Ballistic
        if (mount.weaponType == WeaponAPI.WeaponType.HYBRID || mount.weaponType == WeaponAPI.WeaponType.BALLISTIC || mount.weaponType == WeaponAPI.WeaponType.COMPOSITE || mount.weaponType == WeaponAPI.WeaponType.UNIVERSAL) {
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

            GL11.glVertex2f(x- size / 2, y - size / 2)
            GL11.glVertex2f(x- size / 2, y + height + size / 2)
            GL11.glVertex2f(x + width + size / 2, y + height + size / 2)
            GL11.glVertex2f(x + width + size / 2, y - size / 2)
            GL11.glVertex2f(x- size / 2, y - size / 2)

            GL11.glEnd()
            GL11.glPopMatrix()
        }

        //Energy
        if (mount.weaponType == WeaponAPI.WeaponType.ENERGY || mount.weaponType == WeaponAPI.WeaponType.SYNERGY || mount.weaponType == WeaponAPI.WeaponType.HYBRID || mount.weaponType == WeaponAPI.WeaponType.UNIVERSAL) {
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
            var radius = width * 0.75f + size * 0.75f

            for (i in 0 .. points) {
                var point = MathUtils.getPointOnCircumference(Vector2f(x+width/2, y+height/2), radius, progress)
                progress += arcPer
                GL11.glVertex2f(point.x, point.y)
            }

            GL11.glEnd()
            GL11.glPopMatrix()
        }

        //Missile
        if (mount.weaponType == WeaponAPI.WeaponType.MISSILE || mount.weaponType == WeaponAPI.WeaponType.COMPOSITE || mount.weaponType == WeaponAPI.WeaponType.SYNERGY || mount.weaponType == WeaponAPI.WeaponType.UNIVERSAL) {
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

            GL11.glVertex2f(x- size / 2, y + height / 2 )
            GL11.glVertex2f(x + width / 2 , y + height + size / 2)
            GL11.glVertex2f(x + width + size / 2, y + height / 2 )
            GL11.glVertex2f(x + width / 2, y  - size / 2)
            GL11.glVertex2f(x- size / 2, y + height / 2 )

            GL11.glEnd()
            GL11.glPopMatrix()
        }


        //Arc Rendering
        if (fade > 0f) {

            GL11.glPushMatrix()

            GL11.glTranslatef(0f, 0f, 0f)
            GL11.glRotatef(0f, 0f, 0f, 1f)

            GL11.glDisable(GL11.GL_TEXTURE_2D)

            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)



            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glBegin(GL11.GL_LINE_STRIP)

            var arc = mount.arc
            var angle = mount.angle + 90f
            var start = angle - arc / 2
            var points = 100
            var arcPer = arc / points
            var progress = start
            var alphaPer = 1f / points * 2f
            var swappedAlpha = false
            var alphaMod = 0f

            var radius = width * 4f

            for (i in 0 .. points) {
                var point = MathUtils.getPointOnCircumference(Vector2f(x+width/2, y+height/2), radius, progress)
                progress += arcPer

                GL11.glColor4f(c.red / 255f,
                    c.green / 255f,
                    c.blue / 255f,
                    c.alpha / 255f * (alphaMult * alpha * alphaMod))
                GL11.glVertex2f(point.x, point.y)


                if (!swappedAlpha)alphaMod += alphaPer
                else alphaMod -= alphaPer
                if (alphaMod >= 1) swappedAlpha = true

            }

            GL11.glEnd()
            GL11.glPopMatrix()

            renderArcLines(angle-arc/2f, c, alphaMult * alpha)
            renderArcLines(angle+arc/2f, c, alphaMult * alpha)



        }

        GL11.glLineWidth(1f)

        //Weapon Render Here if the fade is below 0.25
        if (!renderBelow) renderWeapon()
    }

    fun renderWeapon() {
        if (weaponSprite == null) return

        weaponSprite!!.angle = mount.angle
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
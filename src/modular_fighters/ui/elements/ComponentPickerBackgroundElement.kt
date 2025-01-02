package modular_fighters.ui.elements

import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import lunalib.lunaUI.elements.LunaElement
import modular_fighters.ui.LPCDesignerPanel
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11

class ComponentPickerBackgroundElement(var designer: LPCDesignerPanel, tooltip: TooltipMakerAPI, width: Float, height: Float) : LunaElement(tooltip, width, height) {

    init {

    }

    override fun advance(amount: Float) {
        super.advance(amount)
    }

    override fun render(alphaMult: Float) {
        super.render(alphaMult)

    }

    override fun renderBelow(alphaMult: Float) {
        super.renderBelow(alphaMult)
    }

    override fun processInput(events: MutableList<InputEventAPI>?) {
        for (event in events!!)
        {
            if (event.isConsumed) continue
            if (event.isKeyUpEvent && event.eventValue == Keyboard.KEY_ESCAPE)
            {
                event.consume()
                close()
                continue
            }
            if (event.isKeyboardEvent)
            {
                event.consume()
                continue
            }

            if (event.isRMBEvent && event.isMouseDownEvent) {
                event.consume()
                close()
                continue
            }

            if (event.isMouseDownEvent) {
                if (event.x.toFloat() !in x..x+ width ||
                    event.y.toFloat() !in y..y + height) {
                    close()
                    event.consume()
                }
            }

            if (event.isMouseMoveEvent || event.isMouseDownEvent || event.isMouseScrollEvent || event.isMouseUpEvent)
            {
                event.consume()
            }
        }
    }

    fun close() {
        designer.selectedMount = null
        designer.recreatePanel()
    }


}
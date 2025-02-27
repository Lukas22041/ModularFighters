package modular_fighters.misc

import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.ui.UIComponentAPI
import com.fs.starfarer.api.ui.UIPanelAPI

fun UIPanelAPI.getChildrenCopy() : List<UIComponentAPI> {
    return ReflectionUtils.invoke("getChildrenCopy", this) as List<UIComponentAPI>
}

fun UIPanelAPI.getChildrenNonCopy() : MutableList<UIComponentAPI>  {
    return ReflectionUtils.invoke("getChildrenNonCopy", this) as MutableList<UIComponentAPI>
}

fun UIPanelAPI.getWidth() : Float  {
    return ReflectionUtils.invoke("getWidth", this) as Float
}

fun UIPanelAPI.getHeight() : Float  {
    return ReflectionUtils.invoke("getHeight", this) as Float
}

fun UIPanelAPI.clearChildren() {
    ReflectionUtils.invoke("clearChildren", this)
}

fun UIComponentAPI.getParent() : UIPanelAPI?  {
    return ReflectionUtils.invoke("getParent", this) as UIPanelAPI
}

fun TooltipMakerAPI.getParentWidget() : UIComponentAPI? {
    return ReflectionUtils.invoke("getParentWidget", this) as UIPanelAPI
}

fun UIComponentAPI.setOpacity(alpha: Float)
{
    ReflectionUtils.invoke("setOpacity", this, alpha)
}


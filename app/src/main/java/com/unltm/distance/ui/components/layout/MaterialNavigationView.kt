package com.unltm.distance.components.layout

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.navigation.NavigationView
import com.unltm.distance.R

/**
 * 修改过样式的侧边栏
 * @author 王志堃
 */
class MaterialNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.style.Widget_NavigationView,
) : NavigationView(context, attrs, defStyleAttr) {

    private var itemStyle: Int = ITEM_STYLE_DEFAULT

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MaterialNavigationView,
            0, 0
        )

        itemStyle = a.getInteger(
            R.styleable.MaterialNavigationView_itemStyle,
            ITEM_STYLE_DEFAULT
        )
        a.recycle()
        itemBackground = navigationItemBackground()
    }

    private fun navigationItemBackground(): Drawable? {
        // Set Resource
        val resource = when (itemStyle) {
            ITEM_STYLE_DEFAULT -> R.drawable.navigation_item_background_default
            ITEM_STYLE_ROUND_RIGHT -> R.drawable.navigation_item_background_rounded_right
            else -> R.drawable.navigation_item_background_rounded_rect
        }
        var background = AppCompatResources.getDrawable(context, resource)
        if (background != null) {
            val tint = AppCompatResources.getColorStateList(
                context, R.color.navigation_item_background_tint
            )
            background = DrawableCompat.wrap(background.mutate())
            background.setTintList(tint)
        }
        return background
    }

    companion object {
        const val ITEM_STYLE_DEFAULT = 1
        const val ITEM_STYLE_ROUND_RIGHT = 2
        const val ITEM_STYLE_ROUND_RECTANGLE = 3
    }
}
package com.chat.shapeview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * 处理 ShapeTextView / ShapeEditText 的默认 padding 问题。
 *
 * AppCompatTextView / AppCompatEditText 通过 defStyleAttr（textViewStyle / editTextStyle）
 * 从主题继承了默认上下 padding，导致控件高度固定时文字被遮挡。
 *
 * 策略：用户在 XML 显式设置了 android:padding* 时尊重用户值；
 *       未设置时覆盖为 0，消除主题默认 padding。
 */
internal object ShapePaddingManager {

    private val PADDING_ATTRS = intArrayOf(
            android.R.attr.padding,           // 0
            android.R.attr.paddingLeft,       // 1
            android.R.attr.paddingTop,        // 2
            android.R.attr.paddingRight,      // 3
            android.R.attr.paddingBottom,     // 4
            android.R.attr.paddingStart,      // 5
            android.R.attr.paddingEnd,        // 6
            android.R.attr.paddingHorizontal, // 7
            android.R.attr.paddingVertical,   // 8
)

    @SuppressLint("ResourceType")
    fun applyDefaultZeroPadding(view: View, context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, PADDING_ATTRS)
        val allPad        = ta.getDimensionPixelSize(0, -1)
        val userLeft      = ta.getDimensionPixelSize(1, -1)
        val userTop       = ta.getDimensionPixelSize(2, -1)
        val userRight     = ta.getDimensionPixelSize(3, -1)
        val userBottom    = ta.getDimensionPixelSize(4, -1)
        val userStart     = ta.getDimensionPixelSize(5, -1)
        val userEnd       = ta.getDimensionPixelSize(6, -1)
        val horizontalPad = ta.getDimensionPixelSize(7, -1)
        val verticalPad   = ta.getDimensionPixelSize(8, -1)
        ta.recycle()

        val base = if (allPad >= 0) allPad else 0
        val horizontal = if (horizontalPad >= 0) horizontalPad else base
        val vertical = if (verticalPad >= 0) verticalPad else base
        view.setPaddingRelative(
            if (userStart >= 0) userStart else if (userLeft >= 0) userLeft else horizontal,
            if (userTop >= 0) userTop else vertical,
            if (userEnd >= 0) userEnd else if (userRight >= 0) userRight else horizontal,
            if (userBottom >= 0) userBottom else vertical,
        )
    }
}

package com.chat.shapeview

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.chat.shapeview.builder.ShadowDrawableBuilder
import com.chat.shapeview.builder.ShapeDrawableBuilder

class ShapeImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    val shapeDrawableBuilder: ShapeDrawableBuilder
    val shadowDrawableBuilder: ShadowDrawableBuilder
    private val clipPath = Path()
    private val clipRect = RectF()
    private val borderPath = Path()
    private val borderRect = RectF()
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private var shapeReady = false

    init {
        val shapeTa = context.obtainStyledAttributes(attrs, R.styleable.ShapeWidget)
        shapeDrawableBuilder = ShapeDrawableBuilder(this, shapeTa, R.styleable.ShapeWidget)
        shapeTa.recycle()

        val shadowTa = context.obtainStyledAttributes(attrs, R.styleable.ShadowWidget)
        shadowDrawableBuilder = ShadowDrawableBuilder(
            this, shadowTa, R.styleable.ShadowWidget
        ) { shapeDrawableBuilder.radius }
        shadowTa.recycle()

        shapeDrawableBuilder.intoBackground()
        shadowDrawableBuilder.intoShadow()
        shapeReady = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!hasImageClip()) {
            super.onDraw(canvas)
            drawImageBorder(canvas)
            return
        }

        updateClipPathIfNeeded()
        val saveCount = canvas.save()
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
        canvas.restoreToCount(saveCount)
        drawImageBorder(canvas)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (shapeReady && shapeDrawableBuilder.getStrokeSize() > 0f) {
            invalidate()
        }
    }

    private fun hasImageClip(): Boolean {
        return hasImageBorder() ||
                shapeDrawableBuilder.isCircle() ||
                shapeDrawableBuilder.getShape() == GradientDrawable.OVAL ||
                shapeDrawableBuilder.radius > 0f ||
                shapeDrawableBuilder.getTopLeftRadius() > 0f ||
                shapeDrawableBuilder.getTopRightRadius() > 0f ||
                shapeDrawableBuilder.getBottomRightRadius() > 0f ||
                shapeDrawableBuilder.getBottomLeftRadius() > 0f
    }

    private fun updateClipPathIfNeeded() {
        val inset = imageContentInset()
        clipPath.reset()
        clipRect.set(
            paddingLeft + inset,
            paddingTop + inset,
            width - paddingRight - inset,
            height - paddingBottom - inset,
        )
        if (clipRect.isEmpty) return

        if (shapeDrawableBuilder.isCircle()) {
            val radius = minOf(clipRect.width(), clipRect.height()) / 2f
            clipPath.addCircle(clipRect.centerX(), clipRect.centerY(), radius, Path.Direction.CW)
        } else if (shapeDrawableBuilder.getShape() == GradientDrawable.OVAL) {
            clipPath.addOval(clipRect, Path.Direction.CW)
        } else {
            clipPath.addRoundRect(
                clipRect,
                floatArrayOf(
                    borderRadius(shapeDrawableBuilder.getTopLeftRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getTopLeftRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getTopRightRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getTopRightRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getBottomRightRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getBottomRightRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getBottomLeftRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getBottomLeftRadius(), inset),
                ),
                Path.Direction.CW
            )
        }
    }

    private fun hasImageBorder(): Boolean {
        return shapeDrawableBuilder.getStrokeSize() > 0f &&
                shapeDrawableBuilder.getStrokeColorForState(drawableState) != 0
    }

    private fun imageContentInset(): Float = if (hasImageBorder()) {
        shapeDrawableBuilder.getStrokeSize()
    } else {
        0f
    }

    private fun drawImageBorder(canvas: Canvas) {
        val strokeSize = shapeDrawableBuilder.getStrokeSize()
        val strokeColor = shapeDrawableBuilder.getStrokeColorForState(drawableState)
        if (strokeSize <= 0f || strokeColor == 0) return

        updateBorderPath(strokeSize)
        if (!borderPath.isEmpty) {
            updateBorderPaint(strokeSize, strokeColor)
            canvas.drawPath(borderPath, borderPaint)
        }
    }

    private fun updateBorderPaint(strokeSize: Float, strokeColor: Int) {
        borderPaint.strokeWidth = strokeSize
        borderPaint.color = strokeColor
        borderPaint.shader = buildBorderShader()
        val dashSize = shapeDrawableBuilder.getStrokeDashSize()
        val dashGap = shapeDrawableBuilder.getStrokeDashGap()
        borderPaint.pathEffect = if (dashSize > 0f && dashGap > 0f) {
            DashPathEffect(floatArrayOf(dashSize, dashGap), 0f)
        } else {
            null
        }
    }

    private fun buildBorderShader(): Shader? {
        if (!shapeDrawableBuilder.hasStrokeGradient()) return null

        val startColor = shapeDrawableBuilder.getStrokeGradientStartColor()
        val centerColor = shapeDrawableBuilder.getStrokeGradientCenterColor()
        val endColor = shapeDrawableBuilder.getStrokeGradientEndColor()
        val colors = if (centerColor != 0) {
            intArrayOf(startColor, centerColor, endColor)
        } else {
            intArrayOf(startColor, endColor)
        }
        val points = gradientPoints(shapeDrawableBuilder.getStrokeGradientOrientation())
        return LinearGradient(
            points[0],
            points[1],
            points[2],
            points[3],
            colors,
            null,
            Shader.TileMode.CLAMP
        )
    }

    private fun gradientPoints(orientation: Int): FloatArray {
        val left = borderRect.left
        val top = borderRect.top
        val right = borderRect.right
        val bottom = borderRect.bottom
        val centerX = borderRect.centerX()
        val centerY = borderRect.centerY()
        return when (orientation) {
            1 -> floatArrayOf(left, top, right, bottom)
            2 -> floatArrayOf(centerX, top, centerX, bottom)
            3 -> floatArrayOf(right, top, left, bottom)
            4 -> floatArrayOf(right, centerY, left, centerY)
            5 -> floatArrayOf(right, bottom, left, top)
            6 -> floatArrayOf(centerX, bottom, centerX, top)
            7 -> floatArrayOf(left, bottom, right, top)
            else -> floatArrayOf(left, centerY, right, centerY)
        }
    }

    private fun updateBorderPath(strokeSize: Float) {
        val inset = strokeSize / 2f
        borderPath.reset()
        borderRect.set(
            paddingLeft + inset,
            paddingTop + inset,
            width - paddingRight - inset,
            height - paddingBottom - inset,
        )
        if (borderRect.isEmpty) return

        if (shapeDrawableBuilder.isCircle()) {
            val radius = minOf(borderRect.width(), borderRect.height()) / 2f
            borderPath.addCircle(
                borderRect.centerX(),
                borderRect.centerY(),
                radius,
                Path.Direction.CW
            )
        } else if (shapeDrawableBuilder.getShape() == GradientDrawable.OVAL) {
            borderPath.addOval(borderRect, Path.Direction.CW)
        } else {
            borderPath.addRoundRect(
                borderRect,
                floatArrayOf(
                    borderRadius(shapeDrawableBuilder.getTopLeftRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getTopLeftRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getTopRightRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getTopRightRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getBottomRightRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getBottomRightRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getBottomLeftRadius(), inset),
                    borderRadius(shapeDrawableBuilder.getBottomLeftRadius(), inset),
                ),
                Path.Direction.CW
            )
        }
    }

    private fun borderRadius(radius: Float, inset: Float): Float = (radius - inset).coerceAtLeast(0f)
}

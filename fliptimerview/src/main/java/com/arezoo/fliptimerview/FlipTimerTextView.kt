package com.arezoo.fliptimerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView

class FlipTimerTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var alignment = ProperTextAlignment.TOP

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.FlipTimerTextView,
                defStyleAttr, 0
            )

            val alignment = typedArray.getInt(R.styleable.FlipTimerTextView_alignment, 0)
            if (alignment != 0) {
                setAlignment(alignment)
            } else {
                Log.e(
                    "FlipTimerTextView",
                    "You did not set an alignment for an FlipTimerTextView."
                )
            }

            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            val textRect = Rect()
            canvas.getClipBounds(textRect)

            val textRectHeight = textRect.height()
            paint.getTextBounds(this.text.toString(), 0, this.text.length, textRect)
            textRect.offset(-textRect.left, -textRect.top)

            val textHalfHeight = textRect.height().div(2)
            val drawY = when (alignment) {
                ProperTextAlignment.TOP -> {
                    textRect.bottom - textHalfHeight
                }
                ProperTextAlignment.BOTTOM -> {
                    textRectHeight + textHalfHeight
                }
            }.toFloat()
            val drawX = (width / 2).toFloat()

            paint.color = this.currentTextColor
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(this.text.toString(), drawX, drawY, paint)
        }
    }

    private fun setAlignment(textAlignment: Int) {
        alignment = if (textAlignment == 2) {
            ProperTextAlignment.BOTTOM
        } else {
            ProperTextAlignment.TOP
        }
    }

    private enum class ProperTextAlignment {
        TOP,
        BOTTOM
    }
}
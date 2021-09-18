package com.arezoo.fliptimerview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.arezoo.fliptimerview.util.persianDigitsIfPersian
import java.util.*

class FlipTimerDigitView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var animationDuration = ANIMATION_DEFAULT_DURATION

    private val frontUpper: FrameLayout
    private val frontLower: FrameLayout

    private val backUpper: FrameLayout
    private val backLower: FrameLayout

    private val frontUpperText: FlipTimerTextView
    private val frontLowerText: FlipTimerTextView

    private val backUpperText: FlipTimerTextView
    private val backLowerText: FlipTimerTextView

    private var locale = Locale.getDefault()

    init {
        inflate(context, R.layout.view_flip_timer_digit, this).also {
            frontUpper = findViewById(R.id.frontUpper)
            frontLower = findViewById(R.id.frontLower)

            backUpper = findViewById(R.id.backUpper)
            backLower = findViewById(R.id.backLower)

            frontUpperText = findViewById(R.id.frontUpperText)
            frontLowerText = findViewById(R.id.frontLowerText)

            backUpperText = findViewById(R.id.backUpperText)
            backLowerText = findViewById(R.id.backLowerText)
        }

        frontUpperText.measure(0, 0)
        frontLowerText.measure(0, 0)
        backUpperText.measure(0, 0)
        backLowerText.measure(0, 0)
    }

    fun setLocale(locale: Locale) {
        this.locale = locale
    }

    fun setUpperDigitBackground(digitTopDrawable: Drawable) {
        frontUpper.background = digitTopDrawable
        backUpper.background = digitTopDrawable
    }

    fun setBottomDigitBackground(digitTopDrawable: Drawable) {
        frontLower.background = digitTopDrawable
        backLower.background = digitTopDrawable
    }

    fun removeDigitBackground() {
        val transparent = ContextCompat.getColor(context, R.color.transparent)
        frontLower.setBackgroundColor(transparent)
        backLower.setBackgroundColor(transparent)
        frontLower.setBackgroundColor(transparent)
        backLower.setBackgroundColor(transparent)
    }

    fun setDigitTextColor(color: Int) {
        frontUpperText.setTextColor(color)
        frontLowerText.setTextColor(color)
        backLowerText.setTextColor(color)
        backUpperText.setTextColor(color)
    }

    fun setDigitTextSize(size: Float) {
        frontUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        frontLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        backLowerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        backUpperText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setDigitViewSize(width: Int, height: Int) {
        frontUpper.layoutParams = LinearLayout.LayoutParams(width, height)
        backUpper.layoutParams = LinearLayout.LayoutParams(width, height)
        frontLower.layoutParams = LinearLayout.LayoutParams(width, height)
        backLower.layoutParams = LinearLayout.LayoutParams(width, height)
    }

    fun setNewText(newText: String) {
        frontUpper.clearAnimation()
        frontLower.clearAnimation()

        frontUpperText.text = newText.persianDigitsIfPersian(locale)
        frontLowerText.text = newText.persianDigitsIfPersian(locale)
        backUpperText.text = newText.persianDigitsIfPersian(locale)
        backLowerText.text = newText.persianDigitsIfPersian(locale)
    }

    fun animateTextChange(newText: String) {
        if (backUpperText.text == newText) {
            return
        }

        frontUpper.clearAnimation()
        frontLower.clearAnimation()

        val frontPivotX = frontUpper.right - (frontUpper.right - frontUpper.left) / 2

        backUpperText.text = newText
        frontUpper.pivotY = frontUpper.bottom.toFloat()
        frontLower.pivotY = frontUpper.top.toFloat()
        frontUpper.pivotX = frontPivotX.toFloat()
        frontLower.pivotX = frontPivotX.toFloat()

        frontUpper.animate()
            .setDuration(getHalfOfAnimationDuration())
            .rotationX(-ROTATE_90_DEGREE)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                animateDigit()
            }.start()
    }

    private fun animateDigit() {
        frontUpperText.text = backUpperText.text.toString().persianDigitsIfPersian(locale)
        frontUpper.rotationX = 0f
        frontLower.rotationX = ROTATE_90_DEGREE
        frontLowerText.text = backUpperText.text.toString().persianDigitsIfPersian(locale)
        frontLower.animate()
            .setDuration(getHalfOfAnimationDuration())
            .rotationX(0f)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                backLowerText.text = frontLowerText.text.toString()
                    .persianDigitsIfPersian(locale)
            }.start()
    }

    fun setAnimationDuration(duration: Long) {
        this.animationDuration = duration
    }

    private fun getHalfOfAnimationDuration(): Long {
        return animationDuration / 2
    }

    companion object {

        private const val ANIMATION_DEFAULT_DURATION = 600L
        private const val ROTATE_90_DEGREE = 90f
    }
}

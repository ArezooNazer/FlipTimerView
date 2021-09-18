package com.arezoo.fliptimerview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.util.concurrent.*

class FlipTimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var counterDownHelper: CounterDownHelper? = null
    private var countdownTickInterval = COUNTER_DOWN_INTERVAL

    private val digitDays: FlipTimerDigitView
    private val digitHours: FlipTimerDigitView
    private val digitMinute: FlipTimerDigitView
    private val digitSecond: FlipTimerDigitView

    private val doubleZero: String
        get() {
            val zeroString = context.getString(R.string.zero)
            return zeroString + zeroString
        }

    init {
        View.inflate(context, R.layout.view_flip_timer, this).also {
            digitDays = findViewById(R.id.digitDays)
            digitHours = findViewById(R.id.digitHours)
            digitMinute = findViewById(R.id.digitMinute)
            digitSecond = findViewById(R.id.digitSecond)
        }

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.FlipTimerView,
                defStyleAttr,
                0
            )

            typedArray.getDrawable(
                R.styleable.FlipTimerView_digitTopDrawable
            )?.let { digitTopDrawable ->
                setDigitTopDrawable(digitTopDrawable)
            }

            typedArray.getDrawable(
                R.styleable.FlipTimerView_digitBottomDrawable
            )?.let { digitBottomDrawable ->
                setDigitBottomDrawable(digitBottomDrawable)
            }

            typedArray.getColor(
                R.styleable.FlipTimerView_digitTextColor,
                0
            ).also { digitTextColor ->
                setDigitTextColor(digitTextColor)
            }

            typedArray.getDimension(
                R.styleable.FlipTimerView_digitTextSize,
                0f
            ).also { digitTextSize ->
                setDigitTextSize(digitTextSize)
            }

            typedArray.getDimension(
                R.styleable.FlipTimerView_digitPadding,
                0f
            ).also { digitPadding ->
                setDigitPadding(digitPadding.toInt())
            }
            val halfDigitHeight = typedArray.getDimensionPixelSize(
                R.styleable.FlipTimerView_halfDigitHeight,
                0
            )
            val digitWidth = typedArray.getDimensionPixelSize(
                R.styleable.FlipTimerView_digitWidth,
                0
            )
            setHalfDigitHeightAndDigitWidth(halfDigitHeight, digitWidth)

            typedArray.getInt(
                R.styleable.FlipTimerView_animationDuration,
                ANIMATION_DEFAULT_DURATION
            ).also { animationDuration ->
                setAnimationDuration(animationDuration.toLong())
            }

            countdownTickInterval = typedArray.getInt(
                R.styleable.FlipTimerView_countdownTickInterval,
                COUNTER_DOWN_INTERVAL
            )

            typedArray.recycle()
            initializeCounterDownHelper()
        }
    }

    private fun initializeCounterDownHelper() {
        counterDownHelper = CounterDownHelper(countdownTickInterval.toLong())
    }

    fun startCountDown(
        remainingTimeInMill: Long,
        countdownListener: CounterDownCallback? = null
    ) {
        counterDownHelper?.startCountDown(
            remainingTimeInMill,
            countdownListener,
            ::setCountDownTime
        )
    }

    fun resetCountdownTimer() {
        counterDownHelper?.resetCountdownTimer()
        digitDays.setNewText(doubleZero)
        digitHours.setNewText(doubleZero)
        digitMinute.setNewText(doubleZero)
        digitSecond.setNewText(doubleZero)
    }

    private fun setCountDownTime(timeToStart: Long) {

        val days = TimeUnit.MILLISECONDS.toDays(timeToStart)
        val hours = TimeUnit.MILLISECONDS.toHours(
            timeToStart.minus(TimeUnit.DAYS.toMillis(days))
        )
        val minutes = TimeUnit.MILLISECONDS.toMinutes(
            timeToStart.minus(
                TimeUnit.DAYS.toMillis(days) +
                        TimeUnit.HOURS.toMillis(hours)
            )
        )
        val seconds = TimeUnit.MILLISECONDS.toSeconds(
            timeToStart.minus(
                TimeUnit.DAYS.toMillis(days) +
                        TimeUnit.HOURS.toMillis(hours) +
                        TimeUnit.MINUTES.toMillis(minutes)
            )
        )

        digitDays.animateTextChange(getDisplayTimeString(days.toString()))
        digitHours.animateTextChange(getDisplayTimeString(hours.toString()))
        digitMinute.animateTextChange(getDisplayTimeString(minutes.toString()))
        digitSecond.animateTextChange(getDisplayTimeString(seconds.toString()))
    }

    private fun getDisplayTimeString(timeValue: String): String {
        return when (timeValue.length) {
            2 -> timeValue[0].toString() + timeValue[1].toString()
            1 -> ZERO_STRING + timeValue[0]
            else -> doubleZero
        }
    }

    private fun setDigitTopDrawable(digitTopDrawable: Drawable?) {
        if (digitTopDrawable == null) {
            setTransparentBackgroundColor()
            return
        }
        digitDays.setUpperDigitBackground(digitTopDrawable)
        digitHours.setUpperDigitBackground(digitTopDrawable)
        digitMinute.setUpperDigitBackground(digitTopDrawable)
        digitSecond.setUpperDigitBackground(digitTopDrawable)
    }

    private fun setDigitBottomDrawable(digitBottomDrawable: Drawable?) {
        if (digitBottomDrawable == null) {
            setTransparentBackgroundColor()
            return
        }
        digitDays.setBottomDigitBackground(digitBottomDrawable)
        digitHours.setBottomDigitBackground(digitBottomDrawable)
        digitMinute.setBottomDigitBackground(digitBottomDrawable)
        digitSecond.setBottomDigitBackground(digitBottomDrawable)
    }

    private fun setDigitPadding(digitPadding: Int) {
        digitDays.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        digitHours.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        digitMinute.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
        digitSecond.setPadding(digitPadding, digitPadding, digitPadding, digitPadding)
    }

    private fun setDigitTextColor(digitsTextColor: Int) {
        var textColor = digitsTextColor
        if (textColor == 0) {
            textColor = ContextCompat.getColor(context, R.color.transparent)
        }

        digitDays.setDigitTextColor(textColor)
        digitHours.setDigitTextColor(textColor)
        digitMinute.setDigitTextColor(textColor)
        digitSecond.setDigitTextColor(textColor)
    }

    private fun setDigitTextSize(digitsTextSize: Float) {
        digitDays.setDigitTextSize(digitsTextSize)
        digitHours.setDigitTextSize(digitsTextSize)
        digitMinute.setDigitTextSize(digitsTextSize)
        digitSecond.setDigitTextSize(digitsTextSize)
    }

    private fun setHalfDigitHeightAndDigitWidth(halfDigitHeight: Int, digitWidth: Int) {
        digitDays.setDigitViewSize(digitWidth, halfDigitHeight)
        digitHours.setDigitViewSize(digitWidth, halfDigitHeight)
        digitMinute.setDigitViewSize(digitWidth, halfDigitHeight)
        digitSecond.setDigitViewSize(digitWidth, halfDigitHeight)
    }

    private fun setAnimationDuration(animationDuration: Long) {
        digitDays.setAnimationDuration(animationDuration)
        digitHours.setAnimationDuration(animationDuration)
        digitMinute.setAnimationDuration(animationDuration)
        digitSecond.setAnimationDuration(animationDuration)
    }

    private fun setTransparentBackgroundColor() {
        digitDays.removeDigitBackground()
        digitHours.removeDigitBackground()
        digitMinute.removeDigitBackground()
        digitSecond.removeDigitBackground()
    }

    companion object {

        private const val COUNTER_DOWN_INTERVAL = 1000
        private const val ANIMATION_DEFAULT_DURATION = 600
        private const val ZERO_STRING = "0"
    }
}

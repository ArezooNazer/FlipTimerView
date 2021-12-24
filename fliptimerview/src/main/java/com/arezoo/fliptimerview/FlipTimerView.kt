package com.arezoo.fliptimerview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.util.concurrent.TimeUnit

class FlipTimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var counterDownHelper: CounterDownHelper? = null
    private var countdownTickInterval = COUNTER_DOWN_INTERVAL

    private val digits = mutableListOf<FlipTimerDigitView>()

    private val doubleZero: String
        get() {
            val zeroString = context.getString(R.string.zero)
            return zeroString + zeroString
        }

    init {
        View.inflate(context, R.layout.view_flip_timer, this).also {
            digits.add(DAY_INDEX, findViewById(R.id.digitDays))
            digits.add(HOUR_INDEX, findViewById(R.id.digitHours))
            digits.add(MINUTE_INDEX, findViewById(R.id.digitMinute))
            digits.add(SECOND_INDEX, findViewById(R.id.digitSecond))
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
        doOnTimerDigits { setNewText(doubleZero) }
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

        doOnTimerDigits { animateTextChange(getDisplayTimeString(seconds.toString())) }
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
        doOnTimerDigits { setUpperDigitBackground(digitTopDrawable) }
    }

    private fun setDigitBottomDrawable(digitBottomDrawable: Drawable?) {
        if (digitBottomDrawable == null) {
            setTransparentBackgroundColor()
            return
        }
        doOnTimerDigits { setBottomDigitBackground(digitBottomDrawable) }
    }

    private fun setDigitPadding(digitPadding: Int) {
        doOnTimerDigits { setPadding(digitPadding, digitPadding, digitPadding, digitPadding) }
    }

    private fun setDigitTextColor(digitsTextColor: Int) {
        var textColor = digitsTextColor
        if (textColor == 0) {
            textColor = ContextCompat.getColor(context, R.color.transparent)
        }

        doOnTimerDigits { setDigitTextColor(textColor) }
    }

    private fun setDigitTextSize(digitsTextSize: Float) {
        doOnTimerDigits { setDigitTextSize(digitsTextSize) }
    }

    private fun setHalfDigitHeightAndDigitWidth(halfDigitHeight: Int, digitWidth: Int) {
        doOnTimerDigits { setDigitViewSize(digitWidth, halfDigitHeight) }
    }

    private fun setAnimationDuration(animationDuration: Long) {
        doOnTimerDigits { setAnimationDuration(animationDuration) }
    }

    private fun setTransparentBackgroundColor() {
        doOnTimerDigits { removeDigitBackground() }
    }

    private fun doOnTimerDigits(action: FlipTimerDigitView.() -> Unit) {
        digits.forEach { digit -> digit.action() }
    }


    companion object {

        private const val COUNTER_DOWN_INTERVAL = 1000
        private const val ANIMATION_DEFAULT_DURATION = 600
        private const val ZERO_STRING = "0"
        private const val DAY_INDEX = 0
        private const val HOUR_INDEX = 1
        private const val MINUTE_INDEX = 2
        private const val SECOND_INDEX = 3
    }

}

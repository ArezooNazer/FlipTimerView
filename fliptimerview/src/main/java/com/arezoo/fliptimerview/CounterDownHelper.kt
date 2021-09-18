package com.arezoo.fliptimerview

import android.os.CountDownTimer

class CounterDownHelper(
    private val counterDownInterval: Long
) {
    private var countDownTimer: CountDownTimer? = null
    private var countdownListener: CounterDownCallback? = null

    fun startCountDown(
        remainingTimeInMill: Long,
        countdownListener: CounterDownCallback?,
        doOnTick: (Long) -> Unit
    ) {
        countDownTimer?.cancel()
        setCountdownListener(countdownListener)
        initCounterDown(remainingTimeInMill, doOnTick)
        countDownTimer?.start()
    }

    fun resetCountdownTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
        countdownListener = null
    }

    private fun initCounterDown(
        remainingTimeInMill: Long,
        doOnTick: (Long) -> Unit
    ) {
        var hasCalledAlmostFinished = false
        countDownTimer = object : CountDownTimer(remainingTimeInMill, counterDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                if (isTimerAboutToFinish(millisUntilFinished, hasCalledAlmostFinished)) {
                    hasCalledAlmostFinished = true
                    countdownListener?.countdownAboutToFinish()
                }
                doOnTick(millisUntilFinished)
            }

            override fun onFinish() {
                hasCalledAlmostFinished = false
                countdownListener?.countdownFinished()
            }
        }
    }

    private fun setCountdownListener(countdownListener: CounterDownCallback?) {
        this.countdownListener = countdownListener
    }

    private fun isTimerAboutToFinish(
        millisUntilFinished: Long,
        hasCalledAlmostFinished: Boolean
    ): Boolean {
        return millisUntilFinished.div(ONE_SECOND_MILLI) <=
            FINISH_TIMER_THRESHOLD_SEC && !hasCalledAlmostFinished
    }

    companion object {

        private const val ONE_SECOND_MILLI = 1000L
        private const val FINISH_TIMER_THRESHOLD_SEC = 5
    }
}
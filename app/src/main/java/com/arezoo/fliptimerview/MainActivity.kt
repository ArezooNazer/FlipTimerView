package com.arezoo.fliptimerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arezoo.fliptimerview.fliptimer.CounterDownCallback
import com.arezoo.fliptimerview.fliptimer.FlipTimerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFlipTimer()
    }

    private fun initFlipTimer() {
        findViewById<FlipTimerView>(R.id.flipTimerView).apply {
            startCountDown(REMAINING_TIME_MILLI,
                object : CounterDownCallback {
                    override fun countdownAboutToFinish() {
                        super.countdownAboutToFinish()
                    }

                    override fun countdownFinished() {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    companion object {

        private const val REMAINING_TIME_MILLI = 720000L
    }
}
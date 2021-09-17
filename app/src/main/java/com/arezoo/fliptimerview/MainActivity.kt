package com.arezoo.fliptimerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arezoo.fliptimerview.databinding.ActivityMainBinding
import com.arezoo.fliptimerview.fliptimer.CounterDownCallback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFlipTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.flipTimerView.resetCountdownTimer()
    }

    private fun initFlipTimer() {
        binding.flipTimerView.apply {
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
package com.arezoo.fliptimerview

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.arezoo.fliptimerview.databinding.ActivityMainBinding
import com.arezoo.fliptimerview.CounterDownCallback

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
                object : com.arezoo.fliptimerview.CounterDownCallback {
                    override fun countdownAboutToFinish() {
                        super.countdownAboutToFinish()
                    }

                    override fun countdownFinished() {
                        Toast.makeText(
                            this@MainActivity,
                            "time is finished",
                            LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    companion object {

        private const val REMAINING_TIME_MILLI = 99999999L
    }
}
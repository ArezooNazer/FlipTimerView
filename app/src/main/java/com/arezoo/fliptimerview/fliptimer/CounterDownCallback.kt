package com.arezoo.fliptimerview.fliptimer

interface CounterDownCallback {

    fun countdownAboutToFinish() {}
    fun countdownFinished()
}
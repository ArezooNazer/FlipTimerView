# FlipTimerView

A counter down timer for android which supports both dark and light mode and Persian text and digit.

English           |  Perisan
:-------------------------:|:-------------------------:
<img src="https://github.com/ArezooNazer/FlipTimerView/blob/master/demo/english.gif" width=250/>  |  <img src="https://github.com/ArezooNazer/FlipTimerView/blob/master/demo/persian.gif" width=250/>

# Getting started

Step1. Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency

```
dependencies {
	     implementation 'com.github.ArezooNazer:FlipTimerView:1.0.0'
}
```

# Usage

Add it to your layout:

```
 <com.arezoo.fliptimerview.FlipTimerView
        android:id="@+id/flipTimerView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        app:digitBottomDrawable="@drawable/shape_timer_bottom"
        app:digitTextColor="@color/text_color"
        app:digitTextSize="@dimen/title_font_size"
        app:digitTopDrawable="@drawable/shape_timer_top"
        app:digitWidth="40dp"
        app:halfDigitHeight="24dp"/>
```

Then start the timer and implement callbacks:

```
    private fun initFlipTimer() {
        binding.flipTimerView.apply {
            startCountDown(REMAINING_TIME_MILLI,
                object : com.arezoo.fliptimerview.CounterDownCallback {
                    override fun countdownAboutToFinish() {
                        super.countdownAboutToFinish()
                        // optionl
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
```

-----------
Inspired by: https://github.com/anugotta/FlipTimerView

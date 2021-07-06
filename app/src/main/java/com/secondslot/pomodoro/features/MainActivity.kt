package com.secondslot.pomodoro.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.secondslot.pomodoro.R
import com.secondslot.pomodoro.features.stopwatch.ui.StopwatchFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openStopwatchFragment()
    }

    private fun openStopwatchFragment() {
        val fragment = StopwatchFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
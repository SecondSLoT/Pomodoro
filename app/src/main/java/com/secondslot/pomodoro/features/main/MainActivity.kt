package com.secondslot.pomodoro.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.secondslot.pomodoro.R
import com.secondslot.pomodoro.features.foregroundservice.ServiceManager
import com.secondslot.pomodoro.features.timer.ui.TimerFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(ServiceManager(this))

        if (savedInstanceState == null) {
            val fragment = TimerFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }
    }
}
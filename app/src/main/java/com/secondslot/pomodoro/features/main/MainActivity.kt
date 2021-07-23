package com.secondslot.pomodoro.features.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.secondslot.pomodoro.R
import com.secondslot.pomodoro.features.foregroundservice.ForegroundService
import com.secondslot.pomodoro.features.foregroundservice.ServiceManager
import com.secondslot.pomodoro.features.timer.ui.TimerFragment
import com.secondslot.pomodoro.utils.COMMAND_ID
import com.secondslot.pomodoro.utils.COMMAND_STOP

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

    override fun onDestroy() {
        super.onDestroy()

        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        this.startService(stopIntent)
    }
}
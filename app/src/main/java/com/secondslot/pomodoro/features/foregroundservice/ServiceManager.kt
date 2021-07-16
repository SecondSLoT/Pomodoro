package com.secondslot.pomodoro.features.foregroundservice

import android.content.Context
import android.content.Intent
import androidx.core.view.ContentInfoCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.secondslot.pomodoro.eventbus.WorkingTimerEvent
import com.secondslot.pomodoro.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ServiceManager(private val context: Context) : LifecycleObserver {

    private var timerMillis = 0L
    private var timerId = -1

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onTimerEvent(event: WorkingTimerEvent) {
        timerMillis = event.millis
        timerId = event.timerId
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        if (timerId != -1) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.run {
                putExtra(COMMAND_ID, COMMAND_START)
                putExtra(REMAINING_TIMER_MS, timerMillis)
                putExtra(TIMER_ID, timerId)
            }
            context.startService(startIntent)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(context, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        context.startService(stopIntent)
    }
}
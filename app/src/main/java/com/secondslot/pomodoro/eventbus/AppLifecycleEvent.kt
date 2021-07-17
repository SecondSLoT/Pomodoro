package com.secondslot.pomodoro.eventbus

data class AppLifecycleEvent(
    val isAppForeground: Boolean = true
)
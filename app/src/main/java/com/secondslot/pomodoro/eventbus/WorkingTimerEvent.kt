package com.secondslot.pomodoro.eventbus

data class WorkingTimerEvent(
    val timerId: Int = 1,
    val millis: Long = 0
)
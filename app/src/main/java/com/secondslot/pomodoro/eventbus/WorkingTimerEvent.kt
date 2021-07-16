package com.secondslot.pomodoro.eventbus

data class WorkingTimerEvent(
    val timerId: Int,
    val millis: Long = 0
)
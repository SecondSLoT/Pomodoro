package com.secondslot.pomodoro.features.timer.model

data class Timer(
    val id: Int,
    var currentMs: Long,
    val startMs: Long,
    var isStarted: Boolean = false,
    var isFinished: Boolean = false
)

package com.secondslot.pomodoro.features.stopwatch.model

data class Stopwatch(
    val id: Int,
    var currentMs: Long,
    var isStarted: Boolean
)

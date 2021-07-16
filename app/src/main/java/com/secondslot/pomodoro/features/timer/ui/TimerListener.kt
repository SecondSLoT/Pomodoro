package com.secondslot.pomodoro.features.timer.ui

interface TimerListener {

    fun start(id: Int)

    fun stop(id: Int, currentMs: Long)

    fun reset(id: Int, startMs: Long)

    fun delete(id: Int)
}
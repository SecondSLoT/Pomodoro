package com.secondslot.pomodoro.features.timer.vm

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.secondslot.pomodoro.core.LiveDataEventWrapper.LiveDataEvent
import com.secondslot.pomodoro.eventbus.WorkingTimerEvent
import com.secondslot.pomodoro.features.timer.model.Timer
import org.greenrobot.eventbus.EventBus

class TimerFragmentViewModel : ViewModel() {

    val timers = mutableListOf<Timer>()
    var nextId = 0
    private var startedTimerId = -1
    private var countDownTimer: CountDownTimer? = null
    private var isTimerFragmentStarted = true

    private val _updateTimerListLiveData = MutableLiveData<List<Timer>>()
    val updateTimerListLiveData = _updateTimerListLiveData as LiveData<List<Timer>>

    private val _alarmLiveData = MutableLiveData<LiveDataEvent<Int>>()
    val alarmLiveData = _alarmLiveData as LiveData<LiveDataEvent<Int>>

    private val _timeNotSetLiveData = MutableLiveData<LiveDataEvent<Boolean>>()
    val timeNotSetLiveData = _timeNotSetLiveData as LiveData<LiveDataEvent<Boolean>>

    fun onAddNewTimerButtonClicked(startMs: Long) {
        if (startMs != 0L) {
            timers.add(Timer(nextId++, startMs, startMs))
            _updateTimerListLiveData.value = timers
        } else {
            _timeNotSetLiveData.value = LiveDataEvent(true)
        }
    }

    fun onStartTimer(id: Int) {
        if (startedTimerId != -1) {
            val timer = timers.find { it.id == startedTimerId }
            timer?.let { onStopTimer(startedTimerId, timer.currentMs) }
        }

        countDownTimer?.cancel()
        countDownTimer = timers.find { it.id == id}?.let { getCountDownTimer(it) }
        countDownTimer?.start()

        startedTimerId = id
    }

    fun onStopTimer(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false, null)
        unregisterStartedTimer(id)
    }

    fun onResetTimer(id: Int, startMs: Long) {
        changeTimer(id, startMs, isStarted = false, isFinished = false)
        unregisterStartedTimer(id)
    }

    fun onDeleteTimer(id: Int) {
        unregisterStartedTimer(id)
        timers.remove(timers.find { it.id == id })
        _updateTimerListLiveData.value = timers
    }

    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean, isFinished: Boolean?) {
        val timer = timers.find { it.id == id }
        timer?.let {
            val newTimer = Timer(
                id,
                currentMs ?: timer.currentMs,
                timer.startMs,
                isStarted,
                isFinished ?: timer.isFinished
            )
            timers[timers.indexOf(timer)] = newTimer
        }

        _updateTimerListLiveData.value = timers
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {
        return object : CountDownTimer(timer.currentMs, COUNT_DOWN_INTERVAL) {

            override fun onTick(millisUntilFinished: Long) {
                changeTimer(timer.id, millisUntilFinished, isStarted = true, isFinished = false)
                EventBus.getDefault().post(WorkingTimerEvent(timer.id, millisUntilFinished))
            }

            override fun onFinish() {
                changeTimer(timer.id, null, isStarted = false, isFinished = true)
                Log.d("myLogs", "onFinish() called")
                if (isTimerFragmentStarted) _alarmLiveData.value = LiveDataEvent(timer.id)
                unregisterStartedTimer(timer.id)
            }
        }
    }

    private fun unregisterStartedTimer(id: Int) {
        if (id == startedTimerId) {
            countDownTimer?.cancel()
            startedTimerId = -1
            EventBus.getDefault().post(WorkingTimerEvent(startedTimerId))
        }
    }

    fun onTimerFragmentStateChanged(isStarted: Boolean) {
        isTimerFragmentStarted = isStarted
    }

    companion object {
        private const val COUNT_DOWN_INTERVAL = 100L
    }
}
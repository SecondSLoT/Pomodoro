package com.secondslot.pomodoro.features.timer.ui

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.secondslot.pomodoro.R
import com.secondslot.pomodoro.databinding.TimerItemBinding
import com.secondslot.pomodoro.features.timer.model.Timer
import com.secondslot.pomodoro.utils.displayTime

class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: TimerListener,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(timer: Timer) {
        binding.stopwatchTimer.text = timer.currentMs.displayTime()
        binding.progressCircular.setPeriod(timer.startMs)

        if (timer.isFinished) {
            binding.startPauseButton.isEnabled = false
            binding.progressCircular.setCurrent(0L)
            binding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.red_100))
        } else {
            binding.startPauseButton.isEnabled = true
            binding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            binding.progressCircular.setCurrent(timer.currentMs)
        }

        if (timer.isStarted) {
            startTimer(timer)
        } else {
            stopTimer()
        }

        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: Timer) {

        binding.startPauseButton.setOnClickListener {
            if (timer.isStarted) {
                listener.stop(timer.id, timer.currentMs)
            } else {
                listener.start(timer.id)
            }
        }

        binding.resetButton.setOnClickListener {
            if (timer.isStarted) {
                listener.stop(timer.id, timer.currentMs)
            }

            listener.reset(timer.id, timer.startMs)
        }

        binding.deleteButton.setOnClickListener {
            listener.delete(timer.id)
        }
    }

    private fun startTimer(timer: Timer) {
        if (timer.currentMs <= 0L) return
        binding.startPauseButton.text = context.getString(R.string.stop)

        binding.blinkingIndicator.isVisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer() {
        binding.startPauseButton.text = context.getString(R.string.start)

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }
}
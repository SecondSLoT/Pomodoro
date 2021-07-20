package com.secondslot.pomodoro.features.timer.ui

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.TypedValue
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.secondslot.pomodoro.R
import com.secondslot.pomodoro.databinding.ItemTimerBinding
import com.secondslot.pomodoro.features.timer.model.Timer
import com.secondslot.pomodoro.utils.displayTime

class TimerViewHolder(
    private val binding: ItemTimerBinding,
    private val listener: TimerListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(timer: Timer) {
        binding.stopwatchTimer.text = timer.currentMs.displayTime()
        binding.progressCircular.setPeriod(timer.startMs)

        if (timer.isFinished) {
            binding.startPauseButton.isEnabled = false
            val typedValue = TypedValue()
            itemView.context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
            binding.root.setCardBackgroundColor(typedValue.data)
            binding.progressCircular.setCurrent(0L)
        } else {
            binding.startPauseButton.isEnabled = true
            val typedValue = TypedValue()
            itemView.context.theme.resolveAttribute(R.attr.cardBackgroundColor, typedValue, true)
            binding.root.setCardBackgroundColor(typedValue.data)
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
        binding.startPauseButton.text = itemView.context.getString(R.string.stop)

        binding.blinkingIndicator.isVisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun stopTimer() {
        binding.startPauseButton.text = itemView.context.getString(R.string.start)

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }
}
package com.secondslot.pomodoro.features.timer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.secondslot.pomodoro.databinding.ItemTimerBinding
import com.secondslot.pomodoro.features.timer.model.Timer
import com.secondslot.pomodoro.features.timer.ui.TimerListener
import com.secondslot.pomodoro.features.timer.ui.TimerViewHolder


class TimerAdapter(
    private val listener: TimerListener
): ListAdapter<Timer, TimerViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTimerBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Timer>() {

            override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }

            override fun getChangePayload(oldItem: Timer, newItem: Timer) = Any()
        }
    }
}
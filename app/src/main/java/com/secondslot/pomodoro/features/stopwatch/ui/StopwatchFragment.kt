package com.secondslot.pomodoro.features.stopwatch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.secondslot.pomodoro.databinding.FragmentStopwatchBinding
import com.secondslot.pomodoro.features.stopwatch.model.Stopwatch
import com.secondslot.recyclerviewdemoappstopwatch.StopwatchAdapter
import com.secondslot.recyclerviewdemoappstopwatch.StopwatchListener

class StopwatchFragment : Fragment(), StopwatchListener {

    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val stopwatchAdapter = StopwatchAdapter(this)
    private val stopwatches = mutableListOf<Stopwatch>()
    private var nextId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        initViews()
        setListeners()
        return binding.root
    }

    private fun initViews() = binding.run {
        recyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = stopwatchAdapter
        }
    }

    private fun setListeners() {
        binding.addNewStopwatchButton.setOnClickListener {
            stopwatches.add(Stopwatch(nextId++, 0, false))
            stopwatchAdapter.submitList(stopwatches.toList())
        }
    }

    override fun start(id: Int) {
        changeStopwatch(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeStopwatch(id, currentMs, false)
    }

    override fun reset(id: Int) {
        changeStopwatch(id, 0L, false)
    }

    override fun delete(id: Int) {
        stopwatches.remove(stopwatches.find { it.id == id })
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    // Заметьте, что когда мы модифицируем айтем, мы пересоздаём список. Это не очень эффективно.
    // Попробуйте переписать код так, чтобы искомый айтем менялся в списке stopwatches и сабмитайте
    // список в адаптер.
    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<Stopwatch>()
        stopwatches.forEach {
            if (it.id == id) {
                newTimers.add(Stopwatch(it.id, currentMs ?: it.currentMs, isStarted))
            } else {
                newTimers.add(it)
            }
        }

        stopwatchAdapter.submitList(newTimers)
        stopwatches.clear()
        stopwatches.addAll(newTimers)
    }

    companion object {
        fun newInstance(): Fragment = StopwatchFragment()
    }
}
package com.secondslot.pomodoro.features.timer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.secondslot.pomodoro.databinding.FragmentTimerBinding
import com.secondslot.pomodoro.features.timer.model.Timer
import com.secondslot.pomodoro.features.timer.vm.TimerFragmentViewModel

class TimerFragment : Fragment(), TimerListener {

    private val viewModel by viewModels<TimerFragmentViewModel>()
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val timerAdapter = TimerAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        initViews()
        setListeners()
        return binding.root
    }

    private fun initViews() = binding.run {
        recyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timerAdapter
        }
    }

    private fun setListeners() {
        binding.addNewTimerButton.setOnClickListener {
            var startMs = binding.minutesPicker.text.toString().toLongOrNull()?.let {
                binding.minutesPicker.text.toString().toLong() * 1000L * 60L
            } ?: 0L

            startMs += binding.secondsPicker.text.toString().toLongOrNull()?.let {
                binding.secondsPicker.text.toString().toLong() * 1000L
            } ?: 0L

            viewModel.onAddNewTimerButtonClicked(startMs)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        submitTimerList(viewModel.timers)

        // Создание таймеров при запуске приложения для тестов
//        repeat(2) {
//            viewModel.timers.add(Timer(viewModel.nextId++, 300000, 300000))
//        }
//        repeat(3) {
//            viewModel.timers.add(Timer(viewModel.nextId++, 1000, 1000))
//        }
//        submitTimerList(viewModel.timers)
    }

    private fun setObservers() {
        viewModel.updateTimerListLiveData.observe(viewLifecycleOwner, { submitTimerList(it) })
        viewModel.alarmLiveData.observe(viewLifecycleOwner, { showTimerFinishedToast(it) })
    }

    private fun submitTimerList(timers: List<Timer>) {
        timerAdapter.submitList(timers.toList())
    }

    override fun start(id: Int) = viewModel.onStartTimer(id)

    override fun stop(id: Int, currentMs: Long) = viewModel.onStopTimer(id, currentMs)

    override fun reset(id: Int, startMs: Long) = viewModel.onResetTimer(id, startMs)

    override fun delete(id: Int) = viewModel.onDeleteTimer(id)

    private fun showTimerFinishedToast(id: Int) {
        Toast.makeText(
            context,
            "Timer $id: Time is up!",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        fun newInstance(): Fragment = TimerFragment()
    }
}
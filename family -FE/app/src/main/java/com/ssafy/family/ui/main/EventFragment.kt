package com.ssafy.family.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.family.Adapter.TodayScheduleAdapter
import com.ssafy.family.databinding.FragmentEventBinding
import com.ssafy.family.Adapter.OpinionAdapter

class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleAdapter = TodayScheduleAdapter(requireActivity())
        binding.todayScheduleRecyclerView.adapter = scheduleAdapter

        val opinionAdapter = OpinionAdapter(requireActivity())
        binding.opinionRecyclerView.adapter = opinionAdapter
    }
}
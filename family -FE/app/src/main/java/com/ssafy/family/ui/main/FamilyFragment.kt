package com.ssafy.family.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.family.ui.Adapter.StatusAdapter
import com.ssafy.family.databinding.FragmentFamilyBinding

class FamilyFragment : Fragment() {

    private lateinit var binding: FragmentFamilyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFamilyBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StatusAdapter(requireActivity())
        binding.statusRecyclerView.adapter = adapter
    }
}
package com.ssafy.family.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.family.databinding.FragmentFamilyBinding
import com.ssafy.family.ui.Adapter.StatusAdapter
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class FamilyFragment : Fragment() {

    private lateinit var binding: FragmentFamilyBinding
    private val mainFamilyViewModel by activityViewModels<MainFamilyViewModel>()
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
        initView()

    }

    private fun initView() {
        val adapter = StatusAdapter(requireActivity())
        binding.statusRecyclerView.adapter = adapter

        mainFamilyViewModel.getTodayMission()

        mainFamilyViewModel.todayMissionRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    val sb = StringBuilder()
                    sb.append(it.data!!.dataSet!!.missionTarget)
                    sb.append("\n")
                    sb.append(it.data!!.dataSet!!.missionContent)
                    binding.layoutTodayExpression.boxtextTextView.text = sb.toString()
                    dismissLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    dismissLoading()
                }
            }
        }

    }

    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }
}
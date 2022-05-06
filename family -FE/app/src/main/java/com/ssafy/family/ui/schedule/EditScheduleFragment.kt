package com.ssafy.family.ui.schedule

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentEditScheduleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class EditScheduleFragment : Fragment() {

    private lateinit var binding: FragmentEditScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //헤더 및 parent 텍스트 초기화
        (activity as ScheduleActivity).apply {
            changeHeader("일정 수정", "취소", "저장")
            binding.scheduleButtonInclude.button.setOnClickListener {
                requireActivity().finish()
            }
            binding.scheduleTopInclude.backbtn.setOnClickListener {
                requireActivity().finish()
            }
        }
    }
}
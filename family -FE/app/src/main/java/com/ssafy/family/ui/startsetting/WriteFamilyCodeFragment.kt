package com.ssafy.family.ui.startsetting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentAskFamilyCodeBinding
import com.ssafy.family.databinding.FragmentWriteFamilyCodeBinding

class WriteFamilyCodeFragment : Fragment() {

    private lateinit var binding: FragmentWriteFamilyCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteFamilyCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 상단 텍스트 수정
        (activity as StartSettingActivity).changeTopMessage("가족 코드를 입력해주세요!")
        // 버튼별 클릭 이벤트 리스너 등록
        binding.writeFamilyCodeMoveNextBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_in_start_setting, SaveInfoFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.writeFamilyCodeMoveBeforeBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_in_start_setting, AskFamilyCodeFragment())
                .commit()
        }
    } //onViewCreated

}
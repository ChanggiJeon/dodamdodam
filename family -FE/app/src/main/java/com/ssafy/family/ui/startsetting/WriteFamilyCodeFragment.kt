package com.ssafy.family.ui.startsetting

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentAskFamilyCodeBinding
import com.ssafy.family.databinding.FragmentWriteFamilyCodeBinding
import com.ssafy.family.util.Constants.TAG
import com.ssafy.family.util.UiMode

class WriteFamilyCodeFragment : Fragment() {
    private lateinit var binding: FragmentWriteFamilyCodeBinding
    private val familyViewModel by activityViewModels<StartSettingViewModel>()
    lateinit var familyCode: String

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
        binding.writeFamilyCodeMoveNextBtn.setOnClickListener{

            familyCode = binding.writeFamilyCodeInputText.text.toString()
            if (familyCode.count() == 15) {
                handleButtonUI(UiMode.PROGRESS)
                val code = familyCode.uppercase()
                Log.d(TAG, "WriteFamilyCodeFragment - onViewCreated() familyCode : $code")
                familyViewModel.checkFamilyCode(familyCode)
                Log.d(TAG, "WriteFamilyCodeFragment - onViewCreated() familyId : ${familyViewModel.familyId.value}")
            }
        }
    //        binding.writeFamilyCodeMoveNextBtn.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragment_in_start_setting, SaveInfoFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//        binding.writeFamilyCodeMoveBeforeBtn.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragment_in_start_setting, AskFamilyCodeFragment())
//                .commit()
//        }
    } //onViewCreated

    // 프로그래스바 설정
    private fun handleButtonUI(mode: UiMode) {
        when (mode) {
            UiMode.PROGRESS -> {
                binding.writeFamilyCodeBtnProgress.visibility = View.VISIBLE
                binding.writeFamilyCodeMoveNextBtn.visibility = View.INVISIBLE
            }
            UiMode.READY -> {
                binding.writeFamilyCodeBtnProgress.visibility = View.INVISIBLE
                binding.writeFamilyCodeMoveNextBtn.visibility = View.VISIBLE
            }
        }
    }

}
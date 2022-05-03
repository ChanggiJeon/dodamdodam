package com.ssafy.family.ui.startsetting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentAskFamilyCodeBinding
import com.ssafy.family.databinding.FragmentSaveInfoBinding

class SaveInfoFragment : Fragment() {
    val TAG: String = "로그"

    private lateinit var binding: FragmentSaveInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 상단 텍스트 수정
        (activity as StartSettingActivity).changeTopMessage("'나'를 저장하세요!")
        // 스피너 설정
        val roleData = resources.getStringArray(R.array.family_role)
        val adpater = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, roleData)
        binding.saveInfoSpinner.adapter = adpater
        binding.saveInfoSpinner.setSelection(0)
        binding.saveInfoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "선택한 아이템" + binding.saveInfoSpinner.selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

    }

}
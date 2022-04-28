package com.ssafy.family.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.findIdReq
import com.ssafy.family.databinding.FragmentFindIdBinding
import com.ssafy.family.util.InputValidUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindIdFragment : Fragment() {
    lateinit var binding: FragmentFindIdBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFindIdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
    }

    private fun initview() {
        binding.findIdPageTop.scheduleTitle.text = "뒤로가기"
        binding.findIdPageTop.topLayout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.home_frame, LoginFragment())
                .commit()
        }
        binding.findIDPageInputName.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidName(input)) {
                binding.findIDPageInputName.error = null
            }
        }
        binding.findIDPageInputBirthday.addTextChangedListener {
            val input = it.toString()
            if (InputValidUtil.isValidBirthDay(input)) {
                binding.findIDPageInputBirthday.error = null
            }
        }
        binding.findIDPageInputFamilyCode.addTextChangedListener {
            val input = it.toString()
            if (input.length <= 15) {
                binding.findIDPageInputFamilyCode.error = null
            }
        }
        binding.findIDPageFindIDBtn.setOnClickListener {
            val name = binding.findIDPageInputName.text.toString()
            val birthday = binding.findIDPageInputBirthday.text.toString()
            val familycode = binding.findIDPageInputFamilyCode.text.toString()
            if (InputValidUtil.isValidBirthDay(birthday)) {
                if (InputValidUtil.isValidName(name)) {
                    if (familycode.length == 15) {
                        loginViewModel.findId(
                            findIdReq(
                                name,
                                InputValidUtil.makeDay(birthday),
                                familycode
                            )
                        )
                    } else {
                        binding.findIDPageInputFamilyCode.error =
                            "올바르지않은 형식입니다. (ex , 15자리의 영문 숫자 코드)"
                    }
                } else {
                    binding.findIDPageInputName.error = ("올바르지않은 형식입니다.(ex 홍길동)")
                }
            } else {
                binding.findIDPageInputBirthday.error = ("올바르지않은 형식입니다.(ex 20220428)")
            }
        }
        loginViewModel.findLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("ddddd", "initview: "+it.data)
                    if (it.data!!.message == null) {
                        Toast.makeText(requireActivity(),"입력한 정보에 맞는 아이디가 없습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.home_frame, FindPwFragment())
                            .commit()
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "서버 에러", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun findId(findIdReq: findIdReq) {
        loginViewModel.findId(findIdReq)
    }

}
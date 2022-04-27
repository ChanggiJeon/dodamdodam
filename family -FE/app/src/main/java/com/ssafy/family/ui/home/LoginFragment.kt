package com.ssafy.family.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.LoginReq
import com.ssafy.family.databinding.FragmentLoginBinding
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.util.InputValidUtil
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView(){
        binding.loginPageLoginBtn.setOnClickListener {
            if(isValidForm()){
                login()
            }
        }
        binding.loginPageInputID.addTextChangedListener{
            val input = it.toString()
            if(InputValidUtil.isValidId(input)){
                dismissErrorOnId()
            }
        }
        binding.loginPageInputPW.addTextChangedListener{
            val input = it.toString()
            if(InputValidUtil.isValidPassword(input)){
                dismissErrorOnPassword()
            }
        }
        loginViewModel.loginRequestLiveData.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    LoginUtil.setAutoLogin(loginViewModel.isAutoLogin)
                    LoginUtil.saveUserInfo(it.data!!.dataSet!!)
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(),it.message!!, Toast.LENGTH_SHORT)
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }
        }
    }
    private fun login(){
        val id = binding.loginPageInputID.text.toString()
        val pw = binding.loginPageInputPW.text.toString()
        loginViewModel.Login(LoginReq(id,pw))
    }
    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }
    private fun isValidForm(): Boolean {
        val id = binding.loginPageInputID.text.toString()
        val pw = binding.loginPageInputPW.text.toString()
        var flag = 1
        // 이메일 유효성 검사
        if (InputValidUtil.isValidId(id)) {
            dismissErrorOnId()
        } else {
            flag *= 0
            setErrorOnId()
        }
        // 비밀번호 유효성 검사
        if (InputValidUtil.isValidPassword(pw)) {
            dismissErrorOnPassword()
        } else {
            flag *= 0
            setErrorOnPassword()
        }
        // 전부 통과해야 flag == 1
        return flag == 1
    }
    private fun setErrorOnId() {
        binding.textInputLayoutLoginFID.error = resources.getString(R.string.idErrorMessage)
    }

    private fun dismissErrorOnId() {
        binding.textInputLayoutLoginFID.error = null
    }

    private fun dismissErrorOnPassword() {
        binding.textInputLayoutLoginFPW.error = null
    }

    private fun setErrorOnPassword() {
        binding.textInputLayoutLoginFPW.error =
            resources.getString(R.string.passwordErrorMessage)
    }
}
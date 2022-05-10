package com.ssafy.family.ui.main.bottomFragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ssafy.family.config.ApplicationClass
import com.ssafy.family.config.ApplicationClass.Companion.Id
import com.ssafy.family.config.ApplicationClass.Companion.Name
import com.ssafy.family.data.ChatData
import com.ssafy.family.databinding.FragmentChattingBinding
import com.ssafy.family.ui.Adapter.ChattingAdapter
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class ChattingFragment : Fragment() {

    private lateinit var binding: FragmentChattingBinding

    @Inject
    lateinit var chatViewModelFactory: ChatViewModel.FamilyCodeAssistedFactory

    private val viewModel by viewModels<ChatViewModel> {
        ChatViewModel.provideFactory(chatViewModelFactory, familyCode)
    }

    lateinit var chattingAdapter:ChattingAdapter
    val familyCode = "fam_code"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ApplicationClass.isChatting.postValue(true)
        Log.d("dddd", "onAttach: "+ ApplicationClass.isChatting.value)
    }

    override fun onDetach() {
        super.onDetach()
        ApplicationClass.isChatting.postValue(false)
        Log.d("dddd", "onDetach: "+ ApplicationClass.isChatting.value)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView(){
        val context = requireContext()
        chattingAdapter = ChattingAdapter(context, mutableListOf())
        viewModel.initViewModel()

        viewModel.chatLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    chattingAdapter.datas = it.data!!
                    binding.chattingRecyclerView.adapter = chattingAdapter
                    binding.chattingRecyclerView.smoothScrollToPosition(chattingAdapter.itemCount)
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(),it.message!!, Toast.LENGTH_SHORT)
                }
            }
        }

        binding.submitButton.setOnClickListener {
            val message: String = binding.chattingText.text.toString()
            if (message != "") {
                val now: LocalDateTime = LocalDateTime.now()
                val nowTime = now.format(
                    DateTimeFormatter.ofPattern("a h시 mm분").withLocale(Locale.forLanguageTag("ko"))
                )
                val data = ChatData(Id, Name, message, nowTime)
                viewModel.send(data)
                binding.chattingText.setText("")
            }
        }
    }
}
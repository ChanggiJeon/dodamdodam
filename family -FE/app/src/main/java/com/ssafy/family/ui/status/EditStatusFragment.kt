package com.ssafy.family.ui.status

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentEditStatusBinding
import com.ssafy.family.ui.Adapter.StatusEmojiAdapter
import com.ssafy.family.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditStatusFragment : Fragment() {

    private lateinit var binding: FragmentEditStatusBinding
    private lateinit var emojiAdapter: StatusEmojiAdapter
    private val statusViewModel by activityViewModels<StatusViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 가족사진 조회
        statusViewModel.familyPicture.observe(requireActivity()) {
            if (it.data?.dataset?.familyPicture == null){
                Log.d(TAG, "EditStatusFragment - onViewCreated() status : ${it.status}")
                binding.editStatusFamilyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.image_fail))
            } else {
                // TODO: 가족사진 설정 시 조회되는지 확인
//                Glide 활용
                Log.d(TAG, "EditStatusFragment - onViewCreated() status : ${it.status}")
                Log.d(TAG, "EditStatusFragment - onViewCreated() familyPicture : ${it.data.dataset.familyPicture}")
            }
        }
        // 이모지 어댑터 설정
        emojiAdapter = StatusEmojiAdapter(requireActivity())
        binding.statusEmojiRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = emojiAdapter
        }
        emojiAdapter.datas = mutableListOf()
        val emojis = resources.getStringArray(R.array.emoticon)
        for (i in emojis.indices){
            emojiAdapter.datas.add(emojis[i])
            emojiAdapter.checkSelected.add(false)
        }
        // 오늘의 한마디 초기값 입력
        statusViewModel.todaysMessage.observe(requireActivity()) {
            if (it.data?.todaysMessage == null){
                binding.editStatusInputTodaysMessage.text = Editable.Factory.getInstance().newEditable("Default Message")
            } else {
                binding.editStatusInputTodaysMessage.text = Editable.Factory.getInstance().newEditable(it.data.todaysMessage)
            }
        }
        // 확인 버튼 클릭이벤트 리스너 등록
        binding.editStatusConfirmBtn.setOnClickListener {
            val emojiSelected = emojiAdapter.emojiSelected
            val todaysMessage = binding.editStatusInputTodaysMessage.text.toString()
            if (emojiSelected == null) {
                Toast.makeText(requireContext(), "오늘의 기분을 선택해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                statusViewModel.editMyStatus(emotion = emojiSelected, comment = todaysMessage)
            }
        }
    }
}
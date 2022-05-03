package com.ssafy.family.ui.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentAddAlbumBinding
import com.ssafy.family.ui.Adapter.DetailAlbumPhotoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAlbumFragment : Fragment() {
    private lateinit var binding:FragmentAddAlbumBinding

    private val detailAlbumViewModel by activityViewModels<DetailAlbumViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAlbumBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView(){
        detailAlbumViewModel.setTitle("앨범 등록")
        detailAlbumViewModel.setBottomButton("취소", "저장")

    }

}
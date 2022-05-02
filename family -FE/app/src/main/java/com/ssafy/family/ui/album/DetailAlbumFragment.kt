package com.ssafy.family.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.family.data.remote.res.AlbumReaction
import com.ssafy.family.data.remote.res.HashTag
import com.ssafy.family.databinding.FragmentDetailAlbumBinding
import com.ssafy.family.ui.Adapter.AlbumTagAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumCommentAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumEmojiAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumPhotoAdapter
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {
    private lateinit var binding: FragmentDetailAlbumBinding
    private lateinit var photoAdapter: DetailAlbumPhotoAdapter
    private lateinit var emojiAdapter: DetailAlbumEmojiAdapter
    private lateinit var tagAdapter: AlbumTagAdapter
    private lateinit var commentAdapter: DetailAlbumCommentAdapter
    private val detailAlbumViewModel by activityViewModels<DetailAlbumViewModel>()
    //이모지 삭제버튼
    private val commentItemClickListener = object : DetailAlbumCommentAdapter.ItemClickListener{
        override fun onClick(reactionId: Int) {
            detailAlbumViewModel.deleteReaction(reactionId)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //detail 어댑터 3개있는거 싹다 새로
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        detailAlbumView()
    }
    private fun detailAlbumView(){
        detailAlbumViewModel.detailAlbum(detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture.albumId)

        detailAlbumViewModel.detailAlbumRequestLiveData.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS->{
                    tagAdapter.datas = it.data!!.dataSet!!.hashTags as MutableList<HashTag>
                    tagAdapter.notifyDataSetChanged()
                    commentAdapter.datas = it.data!!.dataSet!!.albumReactions as MutableList<AlbumReaction>
                    commentAdapter.notifyDataSetChanged()
                    dismissLoading()
                }
                Status.ERROR->{
                    //테스트용
                    val taglist = mutableListOf<HashTag>()
                    taglist.add(HashTag("#해시"))
                    taglist.add(HashTag("#태그"))
                    taglist.add(HashTag("#해시"))
                    tagAdapter.datas=taglist
                    tagAdapter.notifyDataSetChanged()
                    val templist = mutableListOf<AlbumReaction>()
                    templist.add(AlbumReaction(1,"https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png","https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png","아들",0))
                    templist.add(AlbumReaction(1,"https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png","https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png","아들",1))
                    commentAdapter.datas = templist
                    commentAdapter.notifyDataSetChanged()
                    //테스트용 끝
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }
        }
    }
    private fun initView(){
        photoAdapter = DetailAlbumPhotoAdapter(requireActivity())
        binding.detailAlbumPhotoRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoAdapter
        }

        emojiAdapter = DetailAlbumEmojiAdapter(requireActivity())
        binding.detailAlbumEmojiRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = emojiAdapter
        }

        tagAdapter = AlbumTagAdapter(requireActivity())
        binding.detailAlbumTagRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = tagAdapter
        }

        commentAdapter = DetailAlbumCommentAdapter(requireActivity()).apply {
            itemClickListener = this@DetailAlbumFragment.commentItemClickListener
        }
        binding.detailAlbumCommentRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = commentAdapter
        }
    }
    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }
}
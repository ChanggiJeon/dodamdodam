package com.ssafy.family.ui.album

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.family.R
import com.ssafy.family.data.remote.req.AlbumReactionReq
import com.ssafy.family.data.remote.res.AlbumPicture
import com.ssafy.family.data.remote.res.AlbumReaction
import com.ssafy.family.data.remote.res.HashTag
import com.ssafy.family.databinding.FragmentDetailAlbumBinding
import com.ssafy.family.ui.Adapter.AlbumTagAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumCommentAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumEmojiAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumPhotoAdapter
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {
    private lateinit var binding: FragmentDetailAlbumBinding
    private lateinit var photoAdapter: DetailAlbumPhotoAdapter
    private lateinit var emojiAdapter: DetailAlbumEmojiAdapter
    private lateinit var tagAdapter: AlbumTagAdapter
    private lateinit var commentAdapter: DetailAlbumCommentAdapter
    private var DetailAlbumId by Delegates.notNull<Int>()
    private val detailAlbumViewModel by activityViewModels<DetailAlbumViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()
    //이모지 누르는버튼
    private val emojiItemClickListener = object : DetailAlbumEmojiAdapter.ItemClickListener {
        override fun onClick(item: String) {
            detailAlbumViewModel.addReaction(AlbumReactionReq(item,detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture!!.albumId))
        }

    }

    //이모지 삭제버튼
    private val commentItemClickListener = object : DetailAlbumCommentAdapter.ItemClickListener {
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

    private fun detailAlbumView() {
        detailAlbumViewModel.setTitle("앨범 상세")
        detailAlbumViewModel.detailAlbum(detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture.albumId)
        detailAlbumViewModel.setBottomButton("", "")
        detailAlbumViewModel.detailAlbumRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("dddddddddd", "detailAlbumView: "+it.data!!.dataSet)
                    binding.detailAlbumTitleText.text = it.data!!.dataSet!!.date
                    photoAdapter.datas = it.data!!.dataSet!!.pictures as MutableList<AlbumPicture>
                    photoAdapter.notifyDataSetChanged()
                    tagAdapter.datas = detailAlbumViewModel.saveAlbumLiveData.value!!.hashTags.toMutableList()
                    tagAdapter.notifyDataSetChanged()
                    // TODO: 이모티콘 박아야함 어댑터에 지금 R.drawable.xxx 넣으려고 Int로 해놨는데 나중에 서버에 다 저장해놓고 글라이드로 하기위해서 datas String으로 받게하고 String.xml에 주소 array로 다 박아놔야함
                    emojiAdapter.datas = mutableListOf("https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg","https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg")
                    emojiAdapter.notifyDataSetChanged()
                    commentAdapter.datas =
                        it.data!!.dataSet!!.albumReactions as MutableList<AlbumReaction>
                    commentAdapter.notifyDataSetChanged()
                    dismissLoading()
                }
                Status.ERROR -> {
                    //테스트용
                    binding.detailAlbumTitleText.text = "2022년 5월 2일"
                    val tempphotolist = mutableListOf<AlbumPicture>()
                    tempphotolist.add(
                        AlbumPicture(
                            "https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg",
                            false
                        )
                    )
                    tempphotolist.add(
                        AlbumPicture(
                            "https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg",
                            false
                        )
                    )
                    tempphotolist.add(
                        AlbumPicture(
                            "https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg",
                            false
                        )
                    )
                    photoAdapter.datas = tempphotolist
                    photoAdapter.notifyDataSetChanged()
                    val taglist = mutableListOf<HashTag>()
                    taglist.add(HashTag("#해시"))
                    taglist.add(HashTag("#태그"))
                    taglist.add(HashTag("#해시"))
                    tagAdapter.datas = taglist
                    tagAdapter.notifyDataSetChanged()
                    emojiAdapter.datas = mutableListOf("https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg","https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg")
                    emojiAdapter.notifyDataSetChanged()
                    val templist = mutableListOf<AlbumReaction>()
                    templist.add(
                        AlbumReaction(
                            1,
                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
                            "아들",
                            0
                        )
                    )
                    templist.add(
                        AlbumReaction(
                            1,
                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
                            "아들",
                            1
                        )
                    )
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
                Status.EXPIRED ->{
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    detailAlbumViewModel.detailAlbum(detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture.albumId)
                    dismissLoading()
                }
            }
        }
        detailAlbumViewModel.deleteReactionRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        commentAdapter.datas.removeIf { it.profileId == LoginUtil.getUserInfo()!!.profileId.toInt() }
                    }
                    commentAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    //테스트
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        commentAdapter.datas.removeIf { it.profileId == LoginUtil.getUserInfo()!!.profileId.toInt() }
                    }
                    commentAdapter.notifyDataSetChanged()
                    //테스트 끝
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED ->{
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
            }
        }
        detailAlbumViewModel.addReactionRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    detailAlbumViewModel.detailAlbum(detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture.albumId)
                }
                Status.ERROR -> {
                    //테스트
                    commentAdapter.datas.add(
                        AlbumReaction(
                            1,
                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
                            "아들",
                            1
                        )
                    )
                    commentAdapter.notifyDataSetChanged()
                    //테스트 끝
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED ->{
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
            }
        }
    }

    private fun initView() {
        binding.detailAlbumTitleText.text = ""
        photoAdapter = DetailAlbumPhotoAdapter(requireActivity())
        binding.detailAlbumPhotoRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoAdapter
        }

        emojiAdapter = DetailAlbumEmojiAdapter(requireActivity()).apply {
            itemClickListener = this@DetailAlbumFragment.emojiItemClickListener
        }
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
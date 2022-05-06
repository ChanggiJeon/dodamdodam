package com.ssafy.family.ui.album

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
    private val photoClickListener = object : DetailAlbumPhotoAdapter.ItemClickListener {
        override fun onClick(item: AlbumPicture) {
            showStyleDialog(item.imagePath)
        }

    }
    private val tagItemClickListener = object : AlbumTagAdapter.ItemClickListener {
        override fun onClick(item: HashTag) {
            TODO("Not yet implemented")
        }

    }
    private val emojiItemClickListener = object : DetailAlbumEmojiAdapter.ItemClickListener {
        override fun onClick(item: String) {
            Log.d("dddd", "onClick: " + LoginUtil.getUserInfo()!!.profileId)
            detailAlbumViewModel.addReaction(
                AlbumReactionReq(
                    item,
                    detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture!!.albumId
                )
            )
        }
    }

    //이모지 삭제버튼
    private val commentItemClickListener = object : DetailAlbumCommentAdapter.ItemClickListener {
        override fun onClick(reactionId: Int) {
            Log.d("dddddd", "onClick: " + reactionId)
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
                    val pathlist = arrayListOf<String>()
                    it.data!!.dataSet!!.pictures.forEach { pathlist.add(it.imagePath) }
                    detailAlbumViewModel.paths = pathlist
                    detailAlbumViewModel.hashTag= detailAlbumViewModel.saveAlbumLiveData.value!!.hashTags as ArrayList<HashTag>
                    detailAlbumViewModel.date = detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture!!.date

                    Log.d("dddddddddd", "detailAlbumView: " + it.data!!.dataSet)
                    binding.detailAlbumTitleText.text = it.data!!.dataSet!!.date
                    photoAdapter.datas = it.data!!.dataSet!!.pictures as MutableList<AlbumPicture>
                    photoAdapter.notifyDataSetChanged()
                    tagAdapter.datas =
                        detailAlbumViewModel.hashTag
                    tagAdapter.notifyDataSetChanged()

                    // TODO: 이모티콘 박아야함 어댑터에 지금 R.drawable.xxx 넣으려고 Int로 해놨는데 나중에 서버에 다 저장해놓고 글라이드로 하기위해서 datas String으로 받게하고 String.xml에 주소 array로 다 박아놔야함
                    emojiAdapter.datas = mutableListOf()
                    emojiAdapter.datas.addAll(resources.getStringArray(R.array.emoticon))
                    emojiAdapter.notifyDataSetChanged()
                    commentAdapter.datas =
                        it.data!!.dataSet!!.albumReactions as MutableList<AlbumReaction>
                    commentAdapter.notifyDataSetChanged()
                    dismissLoading()
                }
                Status.ERROR -> {
                    //테스트용
//                    binding.detailAlbumTitleText.text = "2022년 5월 2일"
//                    val tempphotolist = mutableListOf<AlbumPicture>()
//                    tempphotolist.add(
//                        AlbumPicture(
//                            "https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg",
//                            false
//                        )
//                    )
//                    tempphotolist.add(
//                        AlbumPicture(
//                            "https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg",
//                            false
//                        )
//                    )
//                    tempphotolist.add(
//                        AlbumPicture(
//                            "https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg",
//                            false
//                        )
//                    )
//                    photoAdapter.datas = tempphotolist
//                    photoAdapter.notifyDataSetChanged()
//                    val taglist = mutableListOf<HashTag>()
//                    taglist.add(HashTag("#해시"))
//                    taglist.add(HashTag("#태그"))
//                    taglist.add(HashTag("#해시"))
//                    tagAdapter.datas = taglist
//                    tagAdapter.notifyDataSetChanged()
//                    emojiAdapter.datas = mutableListOf(
//                        "https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg",
//                        "https://cdn.topstarnews.net/news/photo/201812/540852_209924_4744.jpg"
//                    )
//                    emojiAdapter.notifyDataSetChanged()
//                    val templist = mutableListOf<AlbumReaction>()
//                    templist.add(
//                        AlbumReaction(
//                            1,
//                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
//                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
//                            "아들",
//                            0
//                        )
//                    )
//                    templist.add(
//                        AlbumReaction(
//                            1,
//                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
//                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
//                            "아들",
//                            1
//                        )
//                    )
//                    commentAdapter.datas = templist
//                    commentAdapter.notifyDataSetChanged()
                    //테스트용 끝
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
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
                    dismissLoading()
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
                Status.EXPIRED -> {
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
//                    commentAdapter.datas.add(
//                        AlbumReaction(
//                            1,
//                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
//                            "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",
//                            "아들",
//                            1
//                        )
//                    )
//                    commentAdapter.notifyDataSetChanged()
                    //테스트 끝
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
            }
        }
        detailAlbumViewModel.deleteAlbumRequestLiveData.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    requireActivity().finish()
                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
                Status.EXPIRED -> {
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "다시 시도해주세요", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
            }
        }
    }

    private fun initView() {
        binding.editAlbumButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.album_frame, UpdateAlbumFragment())
                .commit()
        }
        binding.deleteAlbumButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("앨범 삭제")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("네", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        detailAlbumViewModel.deleteAlbum(detailAlbumViewModel.saveAlbumLiveData.value!!.mainPicture.albumId)
                    }
                })
                .setNegativeButton("아니요", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Log.d("MyTag", "negative")
                    }
                })
                .create()
                .show()

        }
        binding.detailAlbumTitleText.text = ""
        photoAdapter = DetailAlbumPhotoAdapter(requireActivity(), 0).apply {
            itemClickListener = this@DetailAlbumFragment.photoClickListener
        }
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

        tagAdapter = AlbumTagAdapter(requireActivity()).apply {
            itemClickListener = this@DetailAlbumFragment.tagItemClickListener
        }
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

    fun showStyleDialog(url: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_style, null)

        val dialogphoto = dialogView.findViewById<ImageView>(R.id.dialog_image)
        Glide.with(dialogphoto).load(Uri.parse(url))
            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .centerInside()
            .into(dialogphoto)

        val adb = android.app.AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            .setView(dialogView)
        val dialog = adb.create()
        val params: WindowManager.LayoutParams = dialog.window!!.attributes;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.window!!.attributes = params

        //나오는부분말고는 투명하게 해주는것
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show()

    }
}
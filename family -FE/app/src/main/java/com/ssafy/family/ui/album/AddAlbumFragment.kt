package com.ssafy.family.ui.album

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.family.data.remote.res.AlbumPicture
import com.ssafy.family.databinding.FragmentAddAlbumBinding
import com.ssafy.family.ui.Adapter.DetailAlbumPhotoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAlbumFragment : Fragment() {
    private lateinit var binding: FragmentAddAlbumBinding
    private lateinit var photoAdapter: DetailAlbumPhotoAdapter
    private val detailAlbumViewModel by activityViewModels<DetailAlbumViewModel>()

    private val photoClickListener = object :DetailAlbumPhotoAdapter.ItemClickListener{
        override fun onClick(item: AlbumPicture) {
            photoAdapter.datas.forEach { it.main=false }
            photoAdapter.notifyDataSetChanged()
            item.main=true
            photoAdapter.notifyDataSetChanged()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        detailAlbumViewModel.setTitle("앨범 등록")
        detailAlbumViewModel.setBottomButton("취소", "저장")
        photoAdapter = DetailAlbumPhotoAdapter(requireActivity(),false).apply {
            itemClickListener = this@AddAlbumFragment.photoClickListener
        }
        binding.addAlbumPhotoRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoAdapter
        }
        val uriList = detailAlbumViewModel.selectedImgUriList
        val albumpictureList = arrayListOf<AlbumPicture>()
        uriList.forEach { albumpictureList.add(AlbumPicture(getAbsolutePath(it)?:"",false)) }
        photoAdapter.datas = albumpictureList
        photoAdapter.notifyDataSetChanged()
    }
    private fun getAbsolutePath(imgUri: Uri): String? {
        val cursor = context?.contentResolver?.query(imgUri, arrayOf(MediaStore.MediaColumns.DATA), null, null, null)
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        }
        return null
    }
}
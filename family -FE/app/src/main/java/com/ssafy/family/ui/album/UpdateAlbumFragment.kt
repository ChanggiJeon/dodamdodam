package com.ssafy.family.ui.album

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AlbumPicture
import com.ssafy.family.data.remote.res.HashTag
import com.ssafy.family.databinding.FragmentUpdateAlbumBinding
import com.ssafy.family.ui.Adapter.AlbumTagAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumPhotoAdapter
import java.io.File

class UpdateAlbumFragment : Fragment() {

    private lateinit var binding: FragmentUpdateAlbumBinding
    private val detailAlbumViewModel by activityViewModels<DetailAlbumViewModel>()
    private lateinit var photoAdapter: DetailAlbumPhotoAdapter
    private lateinit var tagAdapter: AlbumTagAdapter
    private lateinit var addtag: String

    private val tagItemClickListener = object : AlbumTagAdapter.ItemClickListener {
        override fun onClick(item: HashTag) {
            detailAlbumViewModel.hashTag.remove(item)
            Log.d("ddddd", "tagList: " + detailAlbumViewModel.hashTag)
            tagAdapter.datas = detailAlbumViewModel.hashTag
            tagAdapter.notifyDataSetChanged()
        }

    }
    private val photoClickListener = object : DetailAlbumPhotoAdapter.ItemClickListener {
        override fun onClick(item: AlbumPicture) {
            photoAdapter.datas.forEach { it.main = false }
            detailAlbumViewModel.mainIndex = photoAdapter.datas.indexOf(item)
            Log.d("ddddd", "mainIndex: " + detailAlbumViewModel.mainIndex)
            photoAdapter.notifyDataSetChanged()
            item.main = true
            photoAdapter.notifyDataSetChanged()
        }

    }
    private val deleteClickListener = object : DetailAlbumPhotoAdapter.DeleteClickListener {
        override fun onClick(item: AlbumPicture) {
            if( detailAlbumViewModel.PhotoList.size>1){
                Log.d("dddd", "detailAlbumViewModel.PhotoList: "+detailAlbumViewModel.PhotoList)
                detailAlbumViewModel.pictureIdList.add(item.pictureId)
                Log.d("dddd", "detailAlbumViewModel.pictureIdList "+detailAlbumViewModel.pictureIdList)
                detailAlbumViewModel.PhotoList.remove(item)
                Log.d("dddd", "detailAlbumViewModel.PhotoList: "+detailAlbumViewModel.PhotoList)
                photoAdapter.datas = detailAlbumViewModel.PhotoList
                photoAdapter.notifyDataSetChanged()
            }else{
                Toast.makeText(requireContext(),"최소 한장의 사진이 필요해요",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        picker()
        initView()
    }

    private fun picker() {
        Log.d("ddddd", "picker: " + detailAlbumViewModel.date)
        val date = detailAlbumViewModel.date.split("-")
        val year = date[0].toInt()
        val month = date[1].toInt()-1
        val day = date[2].toInt()
        val datePicker = binding.updateAlbumDatePicker
        binding.updateAlbumTimeText.text = "날짜 : ${year}년 ${month+1}월 ${day}일"
        detailAlbumViewModel.date = "${year}-${month}-${day}"
        datePicker.init(
            year, month, day
        ) { view, year, monthOfYear, dayOfMonth ->
            binding.updateAlbumTimeText.text = "날짜 : ${year}년 ${monthOfYear + 1}월 ${dayOfMonth}일"
            detailAlbumViewModel.date = "${year}-${monthOfYear + 1}-${dayOfMonth}"
            Log.d("ddddd", "date: " + detailAlbumViewModel.date)
        }

    }

    private fun initView() {
        tagAdapter = AlbumTagAdapter(requireActivity()).apply {
            itemClickListener = this@UpdateAlbumFragment.tagItemClickListener
        }
        binding.updateAlbumTagRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = tagAdapter
        }
        detailAlbumViewModel.setTitle("앨범 수정")
        detailAlbumViewModel.setBottomButton("취소", "수정")
        photoAdapter = DetailAlbumPhotoAdapter(requireActivity(), 2).apply {
            itemClickListener = this@UpdateAlbumFragment.photoClickListener
            deleteClickListener = this@UpdateAlbumFragment.deleteClickListener
        }
        binding.updateAlbumPhotoRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = photoAdapter
        }
        val uriList = detailAlbumViewModel.selectedImgUriList

        val albumpictureList =
            detailAlbumViewModel.detailAlbumRequestLiveData.value!!.data!!.dataSet!!.pictures as ArrayList<AlbumPicture>
        Log.d("dddd", "initView:1 "+albumpictureList)
        uriList.forEach { albumpictureList.add(AlbumPicture(getAbsolutePath(it) ?: "", false,0)) }
        Log.d("dddd", "initView:2 "+albumpictureList)
        detailAlbumViewModel.PhotoList = albumpictureList
        photoAdapter.datas = detailAlbumViewModel.PhotoList
        photoAdapter.notifyDataSetChanged()

        val tagList = detailAlbumViewModel.hashTag
        tagAdapter.datas = tagList
        binding.updateAlbumTagEditButton.setOnClickListener {
            val regex = Regex("^[ㄱ-ㅎ가-힣A-Za-z0-9]*$")
            val tagtext = binding.updateAlbumTagEditText.editText?.text.toString()
            if (!tagtext.isNullOrBlank()) {
                if (tagtext.matches(regex)) {
                    addtag = "#" + tagtext
                    if (tagList.size < 3) {
                        tagList.add(HashTag(addtag))
                        detailAlbumViewModel.hashTag = tagList
                        Log.d("ddddd", "tagList: " + detailAlbumViewModel.hashTag)
                        tagAdapter.datas = detailAlbumViewModel.hashTag
                        tagAdapter.notifyDataSetChanged()
                        binding.updateAlbumTagEditText.editText?.setText("")
                    } else {
                        binding.updateAlbumTagEditText.error = "태그는 최대 3개까지에요"
                    }
                } else {
                    binding.updateAlbumTagEditText.error = "특수문자는 사용불가해요"
                }
            } else {
                binding.updateAlbumTagEditText.error = "태그를 입력해주세요"
            }
        }
       binding.updateAddButton.setOnClickListener {
           detailAlbumViewModel.isUpdate=true
           parentFragmentManager.beginTransaction()
               .replace(R.id.album_frame, SelectPhotoFragment())
               .commit()
       }

        val fileList = arrayListOf<File>()
        uriList.forEach { getRealFile(it)?.let { it1 -> fileList.add(it1) } }
        detailAlbumViewModel.files = fileList
        Log.d("ddddd", "fileList: " + detailAlbumViewModel.files)
        val pathList = arrayListOf<String>()
        uriList.forEach { getAbsolutePath(it)?.let { it1 -> pathList.add(it1) } }
        detailAlbumViewModel.paths = pathList
        Log.d("ddddd", "fileList: " + detailAlbumViewModel.paths)
    }

    private fun getRealFile(uri: Uri): File? {
        var uri: Uri? = uri
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        var cursor: Cursor? = uri?.let {
            context?.contentResolver?.query(
                it,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc"
            )
        }
        if (cursor == null || cursor.columnCount < 1) {
            return null
        }
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(column_index)
        if (cursor != null) {
            cursor.close()
            cursor = null
        }
        return File(path)
    }

    private fun getAbsolutePath(imgUri: Uri): String? {
        val cursor = context?.contentResolver?.query(
            imgUri,
            arrayOf(MediaStore.MediaColumns.DATA),
            null,
            null,
            null
        )
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor.getString(0)
            }
        }
        return null
    }

}
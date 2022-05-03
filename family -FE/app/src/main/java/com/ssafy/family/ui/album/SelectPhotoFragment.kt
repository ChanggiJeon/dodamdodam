package com.ssafy.family.ui.album

import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ssafy.family.databinding.FragmentSelectPhotoBinding
import com.ssafy.family.ui.Adapter.PhotoPickAdapter
import com.ssafy.family.ui.Adapter.PhotoRecyclerViewAdapter
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class SelectPhotoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding:FragmentSelectPhotoBinding
    private lateinit var photoRecyclerViewAdapter: PhotoRecyclerViewAdapter
    private val detailAlbumViewModel by activityViewModels<DetailAlbumViewModel>()
    private lateinit var pickAdapter: PhotoPickAdapter
    private val itemClickListener = object: PhotoRecyclerViewAdapter.ItemClickListener {
        override fun onClick(uri: Uri, view: View, position: Int) {
            if(view.tag==true){
                detailAlbumViewModel.deleteImgUri(uri)
                pickAdapter.uris = detailAlbumViewModel.selectedImgUriList
                pickAdapter.notifyDataSetChanged()
            }else{
                Log.d("ddddd", "onClick: "+uri)
                detailAlbumViewModel.setSelectedImgUri(uri)
                pickAdapter.uris = detailAlbumViewModel.selectedImgUriList
                pickAdapter.notifyDataSetChanged()
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
        binding = FragmentSelectPhotoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView(){
        detailAlbumViewModel.setTitle("사진 선택")
        detailAlbumViewModel.setBottomButton("취소","완료")
        photoRecyclerViewAdapter = PhotoRecyclerViewAdapter().apply { itemClickListener = this@SelectPhotoFragment.itemClickListener }
        binding.recyclerViewAlbumF.apply {
            adapter = photoRecyclerViewAdapter
            layoutManager = GridLayoutManager(requireActivity(), 4, RecyclerView.VERTICAL, false)
        }
        pickAdapter = PhotoPickAdapter()
        binding.recyclerViewPick.apply {
            adapter = pickAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onResume() {
        super.onResume()
        reloadImages()
    }
    fun reloadImages() {
        photoRecyclerViewAdapter.uris = setImageUrisFromCursor(getPhotoCursor())
        photoRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun setImageUrisFromCursor(cursor: Cursor):List<Uri> {
        val list = mutableListOf<Uri>()
        cursor.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while(cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                Log.d("dddd", "setImageUrisFromCursor: $uri")
                Log.d("dddd", "setImageUrisFromCursor: ${cursor.getString(3)}")
                list.add(uri)
            }
        }
        return list
    }
    fun getPhotoCursor(): Cursor {
        val resolver = requireActivity().contentResolver
        var queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val img = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATA
        )

        val orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"

        queryUri = queryUri.buildUpon().build()
        return resolver.query(queryUri, img, null, null, orderBy)!!
    }
}
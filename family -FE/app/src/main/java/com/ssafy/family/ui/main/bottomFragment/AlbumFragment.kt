package com.ssafy.family.ui.main.bottomFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.ssafy.family.data.remote.res.*
import com.ssafy.family.ui.Adapter.AlbumMonthAdapter
import com.ssafy.family.databinding.FragmentAlbumBinding
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

// mainactivity - albumfragment : 월 리스트 (2021.06, 2021,07 ...)
@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var albumMonthAdapter: AlbumMonthAdapter
    private val albumViewModel by activityViewModels<AlbumViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        findAllAlbum()
        albumMonthAdapter = AlbumMonthAdapter(requireActivity())
        binding.monthRecyclerView.adapter = albumMonthAdapter
        albumViewModel.allAlbumRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    albumMonthAdapter.datas = (it.data!!.dataSet as MutableList<AllAlbum>?)!!
                    albumMonthAdapter.notifyDataSetChanged()
                    dismissLoading()
                }
                Status.ERROR -> {
                    //테스트용
                    val taglist = mutableListOf<HashTag>()
                    taglist.add(HashTag("#해시"))
                    taglist.add(HashTag("#해시"))
                    val albumlist = mutableListOf<Album>()
                    albumlist.add(Album(0, mutableListOf(),"2022년 5월 2일"))
                    val temppicture = Picture(1,albumlist,"d","https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png",true)
                    val allalbumlist = mutableListOf<AllAlbum>()
                    allalbumlist.add(AllAlbum(taglist,temppicture))
                    allalbumlist.add(AllAlbum(taglist,temppicture))
                    allalbumlist.add(AllAlbum(taglist,temppicture))
                    albumMonthAdapter.datas= (allalbumlist)
                    albumMonthAdapter.notifyDataSetChanged()
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

    private fun findAllAlbum() {
        albumViewModel.findAllAlbum()
    }

    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }
}
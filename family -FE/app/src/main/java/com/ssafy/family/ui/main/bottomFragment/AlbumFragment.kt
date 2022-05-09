package com.ssafy.family.ui.main.bottomFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.data.remote.res.HashTag
import com.ssafy.family.data.remote.res.Picture
import com.ssafy.family.databinding.FragmentAlbumBinding
import com.ssafy.family.ui.Adapter.AlbumMonthAdapter
import com.ssafy.family.ui.Adapter.PagerAdapter
import com.ssafy.family.ui.album.AlbumActivity
import com.ssafy.family.ui.album.SearchDateFragment
import com.ssafy.family.ui.album.SearchTagFragment
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.main.EventFragment
import com.ssafy.family.ui.main.FamilyFragment
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

// mainactivity - albumfragment : 월 리스트 (2021.06, 2021,07 ...)
@AndroidEntryPoint
class AlbumFragment : Fragment() {
    companion object {
        val FRIEND_INFO = "friendInfo"
    }

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var albumMonthAdapter: AlbumMonthAdapter
    private val albumViewModel by activityViewModels<AlbumViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private val itemClickListener = object : AlbumMonthAdapter.ItemClickListener {
        override fun onClick(allAlbum: AllAlbum) {
            val intent = Intent(requireActivity(), AlbumActivity::class.java)
            intent.putExtra(FRIEND_INFO, allAlbum)
            startActivity(intent)
        }
    }
    val onScrollListener = object : RecyclerView.OnScrollListener() {
        var temp: Int = 0
        private var isScrolledDown = false
        override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isScrolledDown = dy < 0;

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            Log.d("ddddddd", "onScrollStateChanged: " + newState)
            if (!isScrolledDown) {
                binding.tabs.visibility = View.GONE
                binding.viewpager.visibility = View.GONE
            }
            if (isScrolledDown) {
                binding.tabs.visibility = View.VISIBLE
                binding.viewpager.visibility = View.VISIBLE

            }

        }
    }
    
    override fun onResume() {
        super.onResume()
        findAllAlbum()
    }
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
        val adapter = PagerAdapter(requireActivity())
        adapter.addFragment(SearchTagFragment(), "태그별")
        adapter.addFragment(SearchDateFragment(), "날짜별")
        binding.viewpager.adapter = adapter
        binding.viewpager.currentItem = 0
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
        binding.addAlbumButton.setOnClickListener {
            val intent = Intent(requireActivity(), AlbumActivity::class.java)
            startActivity(intent)
        }
        albumMonthAdapter = AlbumMonthAdapter(requireActivity()).apply {
            itemClickListener = this@AlbumFragment.itemClickListener
        }
        binding.monthRecyclerView.addOnScrollListener(onScrollListener)
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
//                    val taglist = mutableListOf<HashTag>()
//                    taglist.add(HashTag("#해시"))
//                    taglist.add(HashTag("#해시"))
//                    val temppicture = Picture(
//                        1,
//                        "2022년 5월 2일",
//                        "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png"
//                    )
//                    val allalbumlist = mutableListOf<AllAlbum>()
//                    allalbumlist.add(AllAlbum(taglist, temppicture))
//                    allalbumlist.add(AllAlbum(taglist, temppicture))
//                    allalbumlist.add(AllAlbum(taglist, temppicture))
//                    albumMonthAdapter.datas = (allalbumlist)
//                    albumMonthAdapter.notifyDataSetChanged()
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
                    findAllAlbum()
                    dismissLoading()
                }
            }
        }

        albumViewModel.searchAlbumRequestLiveData.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {
                    albumMonthAdapter.datas = (it.data!!.dataSet as MutableList<AllAlbum>?)!!
                    albumMonthAdapter.notifyDataSetChanged()
                    dismissLoading()
                }
                Status.ERROR -> {
                    //테스트용
//                    val taglist = mutableListOf<HashTag>()
//                    taglist.add(HashTag("#해시"))
//                    taglist.add(HashTag("#해시"))
//                    val temppicture = Picture(
//                        1,
//                        "2022년 5월 2일",
//                        "https://cdn.pixabay.com/photo/2019/08/01/12/36/illustration-4377408_960_720.png"
//                    )
//                    val allalbumlist = mutableListOf<AllAlbum>()
//                    allalbumlist.add(AllAlbum(taglist, temppicture))
//                    allalbumlist.add(AllAlbum(taglist, temppicture))
//                    allalbumlist.add(AllAlbum(taglist, temppicture))
//                    albumMonthAdapter.datas = (allalbumlist)
//                    albumMonthAdapter.notifyDataSetChanged()
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
                    val keyword = binding.viewpager[0].findViewById<EditText>(R.id.search_edit_text).text.toString()
                    albumViewModel.searchAlbum(keyword)
                    dismissLoading()
                }
            }

        }
        albumViewModel.searchDateAlbumRequestLiveData.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS->{
                    albumMonthAdapter.datas = (it.data!!.dataSet as MutableList<AllAlbum>?)!!
                    albumMonthAdapter.notifyDataSetChanged()
                    dismissLoading()
                }
                Status.LOADING->{
                    setLoading()

                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message ?: "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                    dismissLoading()
                }
                Status.EXPIRED -> {
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    val year = binding.viewpager[1].findViewById<EditText>(R.id.search_edit_year).text.toString()
                    val month = binding.viewpager[1].findViewById<EditText>(R.id.search_edit_month).text.toString()
                    albumViewModel.searchDateAlbum(year+month)
                    dismissLoading()
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
package com.ssafy.family.ui.main.bottomFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.family.data.remote.req.AddFcmReq
import com.ssafy.family.ui.Adapter.AlbumMonthAdapter
import com.ssafy.family.databinding.FragmentAlbumBinding
import com.ssafy.family.ui.Adapter.AlbumAdapter
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint

// mainactivity - albumfragment : 월 리스트 (2021.06, 2021,07 ...)
@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var albumMonthAdapter:AlbumMonthAdapter
    private lateinit var albumAdapter: AlbumAdapter
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
    private fun initView(){
        findAllAlbum()
        albumMonthAdapter = AlbumMonthAdapter(requireActivity())
        albumAdapter = AlbumAdapter(requireContext())
        binding.monthRecyclerView.adapter = albumMonthAdapter
        albumViewModel.allAlbumRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    it.data!!.dataSet!!.forEach {
                        albumMonthAdapter.AlbumAdapter.datas.add(it)
                        albumAdapter.notifyDataSetChanged()
                        albumMonthAdapter.notifyDataSetChanged()
                    }

                    dismissLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), it.message?:"서버 에러", Toast.LENGTH_SHORT).show()
                    dismissLoading()
                }
                Status.LOADING -> {
                    setLoading()
                }
            }
        }
    }
    private fun findAllAlbum(){
        albumViewModel.findAllAlbum()
    }
    private fun setLoading() {
        binding.progressBarLoginFLoading.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding.progressBarLoginFLoading.visibility = View.GONE
    }
}
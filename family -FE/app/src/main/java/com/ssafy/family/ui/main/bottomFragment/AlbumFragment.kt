package com.ssafy.family.ui.main.bottomFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.family.Adapter.AlbumMonthAdapter
import com.ssafy.family.databinding.FragmentAlbumBinding

// mainactivity - albumfragment : 월 리스트 (2021.06, 2021,07 ...)
class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding

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

        val albumMonthAdapter = AlbumMonthAdapter(requireActivity())
        binding.monthRecyclerView.adapter = albumMonthAdapter

    }
}
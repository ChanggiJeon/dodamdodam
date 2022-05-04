package com.ssafy.family.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.family.databinding.FragmentSearchDateBinding
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.main.bottomFragment.AlbumViewModel
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*

class SearchDateFragment : Fragment() {

    private lateinit var binding: FragmentSearchDateBinding
    private val albumViewModel by activityViewModels<AlbumViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init(){
        val now = System.currentTimeMillis()
        val date = Date(now)
        val yearformat = SimpleDateFormat("yyyy")
        val monthformat = SimpleDateFormat("MM")
        binding.searchEditYear.hint = yearformat.format(date)
        binding.searchEditMonth.hint = monthformat.format(date)
    }

}
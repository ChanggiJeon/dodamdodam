package com.ssafy.family.ui.startsetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentAskFamilyCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskFamilyCodeFragment : Fragment() {

    private lateinit var binding: FragmentAskFamilyCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAskFamilyCodeBinding.inflate(inflater, container, false)
        return binding.root
    }
}
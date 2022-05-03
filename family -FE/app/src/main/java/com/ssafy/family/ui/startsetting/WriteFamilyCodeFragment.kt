package com.ssafy.family.ui.startsetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.family.R
import com.ssafy.family.databinding.FragmentAskFamilyCodeBinding
import com.ssafy.family.databinding.FragmentWriteFamilyCodeBinding

class WriteFamilyCodeFragment : Fragment() {

    private lateinit var binding: FragmentWriteFamilyCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteFamilyCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

}
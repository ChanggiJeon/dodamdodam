package com.ssafy.family.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.family.R
import com.ssafy.family.databinding.ActivityHomeBinding
import com.ssafy.family.ui.schedule.AddScheduleFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_frame, LoginFragment())
            .commit()
    }
}
package com.ssafy.family.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.family.R
import com.ssafy.family.databinding.ActivityHomeBinding
import com.ssafy.family.ui.schedule.AddScheduleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("ddddddd", "onCreate: "+FirebaseMessaging.getInstance().token.result)
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_frame, LoginFragment())
            .commit()


    }
}
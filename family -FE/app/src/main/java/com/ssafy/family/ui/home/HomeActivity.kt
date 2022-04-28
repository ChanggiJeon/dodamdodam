package com.ssafy.family.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.family.R
import com.ssafy.family.config.ApplicationClass
import com.ssafy.family.databinding.ActivityHomeBinding
import com.ssafy.family.ui.main.MainActivity
import com.ssafy.family.ui.schedule.AddScheduleFragment
import com.ssafy.family.util.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(ApplicationClass.sSharedPreferences.getString(ApplicationClass.JWT)!=null){
            // TODO: 첫 접속일시 분기 만들어야함
            // TODO: 토큰 만료됐을시 분기 만들어야함
            startActivity(Intent(this,MainActivity::class.java))
        }
        //Log.d("ddddddd", "onCreate: "+FirebaseMessaging.getInstance().token.result)
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_frame, LoginFragment())
            .commit()


    }
}
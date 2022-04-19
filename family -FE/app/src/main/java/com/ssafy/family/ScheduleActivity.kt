package com.ssafy.family

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.family.databinding.ActivityMainBinding
import com.ssafy.family.databinding.ActivityScheduleBinding

class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.schedule_frame, AddScheduleFragment())
            .commit()

    }
}
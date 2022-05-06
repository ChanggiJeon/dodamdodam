package com.ssafy.family.ui.startsetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.ssafy.family.R
import com.ssafy.family.databinding.ActivityStartSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_in_start_setting, AskFamilyCodeFragment())
            .commit()
    }
    // 상단 메시지 변경하는 함수
    fun changeTopMessage(text: String) {
        binding.startSettingTopMsg.text = text
    }

}
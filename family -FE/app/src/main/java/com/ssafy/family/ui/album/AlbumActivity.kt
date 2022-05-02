package com.ssafy.family.ui.album

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.databinding.ActivityAlbumBinding
import com.ssafy.family.ui.main.bottomFragment.AlbumFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumBinding
    private val detailAlbumViewModel by viewModels<DetailAlbumViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getParcelableExtra<AllAlbum>(AlbumFragment.FRIEND_INFO)?.let {
            detailAlbumViewModel.setDetailAlbum(it)
            Log.d("dddddd", "onCreate: "+it)
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        supportFragmentManager.beginTransaction()
            .replace(R.id.album_frame, DetailAlbumFragment())
            .commit()

    }
}
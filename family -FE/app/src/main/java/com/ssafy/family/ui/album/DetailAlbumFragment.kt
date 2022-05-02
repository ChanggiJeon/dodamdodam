package com.ssafy.family.ui.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.family.ui.Adapter.AlbumTagAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumCommentAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumEmojiAdapter
import com.ssafy.family.ui.Adapter.DetailAlbumPhotoAdapter
import com.ssafy.family.databinding.FragmentDetailAlbumBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {
    private lateinit var binding: FragmentDetailAlbumBinding
    private lateinit var photoAdapter:DetailAlbumPhotoAdapter
    private lateinit var emojiAdapter: DetailAlbumEmojiAdapter
    private lateinit var tagAdapter: AlbumTagAdapter
    private lateinit var commentAdapter: DetailAlbumCommentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailAlbumBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoAdapter = DetailAlbumPhotoAdapter(requireActivity())
        binding.detailAlbumPhotoRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
            adapter =  photoAdapter
        }

        emojiAdapter = DetailAlbumEmojiAdapter(requireActivity())
        binding.detailAlbumEmojiRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
            adapter =  emojiAdapter
        }

        tagAdapter = AlbumTagAdapter(requireActivity())
        binding.detailAlbumTagRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
            adapter =  tagAdapter
        }

        commentAdapter = DetailAlbumCommentAdapter(requireActivity())
        binding.detailAlbumCommentRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter =  commentAdapter
        }
    }
}
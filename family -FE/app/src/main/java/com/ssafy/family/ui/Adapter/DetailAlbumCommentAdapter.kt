package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.R
import com.ssafy.family.databinding.DetailAlbumCommentListBinding

class DetailAlbumCommentAdapter(private val context: Context) : RecyclerView.Adapter<DetailAlbumCommentAdapter.ViewHolder>()  {

    private var datas = mutableListOf<Int>(1,2,3)
    lateinit var itemClickListener: ItemClickListener
    inner class ViewHolder(val binding: DetailAlbumCommentListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.commentFamilyImg.setImageResource(R.drawable.amusing)
            binding.commentFamilyEmoji.setImageResource(R.drawable.laughing)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.item_family_status,parent,false)
        val inflater = LayoutInflater.from(parent.context)
        val binding = DetailAlbumCommentListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
    interface ItemClickListener {
        fun onClick()
    }
}
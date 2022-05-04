package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AlbumPicture
import com.ssafy.family.databinding.AlbumPhotoListBinding

class DetailAlbumPhotoAdapter(private val context: Context,private val isDetail:Boolean) :
    RecyclerView.Adapter<DetailAlbumPhotoAdapter.ViewHolder>() {

    var datas = mutableListOf<AlbumPicture>()
    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(val binding: AlbumPhotoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumPicture) {
            Glide.with(itemView).load(item.imagePath).into(binding.albumPhotoListImg)
            binding.albumPhotoListImg.setOnClickListener {
                itemClickListener.onClick(item)
            }
            if(isDetail){
                itemView.background = null
            }else{
                if (item.main) {
                    itemView.background = ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.list_box_select,
                        null
                    )
                } else {
                    itemView.background = null
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.item_family_status,parent,false)
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlbumPhotoListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    interface ItemClickListener {
        fun onClick(item: AlbumPicture)
    }


}

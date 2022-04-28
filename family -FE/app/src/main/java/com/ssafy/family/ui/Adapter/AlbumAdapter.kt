package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.data.remote.res.AllAlbum
import com.ssafy.family.databinding.ItemAlarmBinding
import com.ssafy.family.databinding.ItemAlbumBinding

// mainactivity - familyfragment : 알림 보내기 메시지 리스트
class AlbumAdapter(private val context: Context) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    var datas = mutableListOf<AllAlbum>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.item_family_status,parent,false)
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlbumBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
//
//        val current = getItemId(position)
//        val itemBinding = holder.binding as ItemAlbumBinding
//        itemBinding.familyStatus.setOnClickListener {
//            if(itemBinding.alarmRecycler.visibility == VISIBLE){
//                itemBinding.alarmRecycler.visibility = GONE
//            }else{
//                itemBinding.alarmRecycler.visibility = VISIBLE
//            }
//        }

//        itemBinding.executePendingBindings()
//        holder.itemView.apply{
//            setOnClickListener{
////                val intent = Intent(context, DetailActivity::class.java)
////                intent.putExtra("bookId", currentBook.bookId)
////                ContextCompat.startActivity(context, intent, null)
//            }
//        }
    }

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        private val albumtag: TextView = itemView.findViewById(R.id.album_tag_text)
        private val albumimg: ImageView = itemView.findViewById(R.id.album_img)

        fun bind(item: AllAlbum) {
            var Alltag = ""
            item.hashTags.forEach { Alltag+=it }
            albumtag.text = Alltag
            Glide.with(itemView).load(item.mainPicture.path_name).into(albumimg)

        }
    }


}
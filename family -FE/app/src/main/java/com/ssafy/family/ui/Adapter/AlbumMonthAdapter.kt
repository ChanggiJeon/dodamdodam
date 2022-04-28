package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.AlbumRes
import com.ssafy.family.databinding.ItemMonthAlbumBinding

// mainactivity - albumfragment : 월 리스트 (2021.06, 1,2,3 ...)
class AlbumMonthAdapter(private val context: Context) :
    RecyclerView.Adapter<AlbumMonthAdapter.ViewHolder>() {

    var datas = mutableListOf<AlbumRes>()
    lateinit var AlbumAdapter: AlbumAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.item_family_status,parent,false)
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMonthAlbumBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

//        val currentBook = getItemId(position)
//        val itemBinding = holder.binding as ItemAlarmBinding
//        itemBinding.familyStatus.setOnClickListener {
//            if(itemBinding.alarmRecycler.visibility == VISIBLE){
//                itemBinding.alarmRecycler.visibility = GONE
//            }else{
//                itemBinding.alarmRecycler.visibility = VISIBLE
//            }
//        }

        val itemBinding = holder.binding as ItemMonthAlbumBinding
        AlbumAdapter = AlbumAdapter(context)
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        itemBinding.albumRecycler.layoutManager = layoutManager
        itemBinding.albumRecycler.adapter = AlbumAdapter
        //itemBinding.executePendingBindings()
//        holder.itemView.apply{
//            setOnClickListener{
////                val intent = Intent(context, DetailActivity::class.java)
////                intent.putExtra("bookId", currentBook.bookId)
////                ContextCompat.startActivity(context, intent, null)
//            }
//        }
    }

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

//        private val txtName: TextView = itemView.findViewById(R.id.tv_rv_name)
//        private val txtAge: TextView = itemView.findViewById(R.id.tv_rv_age)
//        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)
        private val albumrecycler:RecyclerView = itemView.findViewById(R.id.album_recycler)
        fun bind(item: AlbumRes) {
            albumrecycler.adapter = AlbumAdapter

        }
    }


}
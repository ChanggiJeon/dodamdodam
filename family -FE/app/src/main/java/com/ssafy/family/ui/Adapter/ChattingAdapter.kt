package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.databinding.ItemChattingBinding

// mainactivity - familyfragment : 알림 보내기 메시지 리스트
class ChattingAdapter(private val context: Context) : RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {

    private var datas = mutableListOf<Int>(1,1,2)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.item_family_status,parent,false)
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChattingBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        holder.bind(datas[position])
        val itemBinding = holder.binding as ItemChattingBinding
        if(datas[position] == 1){
            itemBinding.ownChatting.visibility = GONE
            itemBinding.oppChatting.visibility = VISIBLE
        }else{
            itemBinding.ownChatting.visibility = VISIBLE
            itemBinding.oppChatting.visibility = GONE
        }
//        val currentBook = getItemId(position)
//        val itemBinding = holder.binding as ItemAlarmBinding
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

//        private val txtName: TextView = itemView.findViewById(R.id.tv_rv_name)
//        private val txtAge: TextView = itemView.findViewById(R.id.tv_rv_age)
//        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)
//
//        fun bind(item: ProfileData) {
//            txtName.text = item.name
//            txtAge.text = item.age.toString()
//            Glide.with(itemView).load(item.img).into(imgProfile)
//
//        }
    }


}
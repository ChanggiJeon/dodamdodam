package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.config.ApplicationClass.Companion.Id
import com.ssafy.family.data.ChatData
import com.ssafy.family.databinding.ItemChattingBinding

// mainactivity - chatfragment : 채팅 리스트
class ChattingAdapter(private val context: Context, var datas: MutableList<ChatData>) : RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChattingBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBinding = holder.binding as ItemChattingBinding
        if(datas[position].id == Id){
            itemBinding.ownChatting.visibility = VISIBLE
            itemBinding.oppChatting.visibility = GONE
            itemBinding.ownChattingText.text = datas[position].message.toString()
            itemBinding.ownTimeText.text = datas[position].time.toString()
        }else{
            itemBinding.ownChatting.visibility = GONE
            itemBinding.oppChatting.visibility = VISIBLE
            itemBinding.oppChattingText.text = datas[position].message.toString()
            itemBinding.oppTimeText.text = datas[position].time.toString()
            itemBinding.oppWriterText.text = datas[position].name.toString()
        }
    }

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {}

}
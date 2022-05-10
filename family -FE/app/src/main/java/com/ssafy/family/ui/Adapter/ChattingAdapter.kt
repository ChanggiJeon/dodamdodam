package com.ssafy.family.ui.Adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ssafy.family.data.ChatData
import com.ssafy.family.data.remote.res.MemberInfo
import com.ssafy.family.databinding.ItemChattingBinding
import com.ssafy.family.util.LoginUtil.getUserInfo

// mainactivity - chatfragment : 채팅 리스트
class ChattingAdapter(var memberList:List<MemberInfo>, var datas: MutableList<ChatData>) : RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChattingBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBinding = holder.binding as ItemChattingBinding

        var user: MemberInfo? = null
        for(a in memberList){
            if(a.profileId == datas[position].id){
                user = a
                break
            }
        }

        if(user != null){
            if(user.profileId == getUserInfo()!!.profileId){
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
                Glide.with(itemBinding.oppProfileButton).load(datas[position].profileImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .centerInside()
                    .into(itemBinding.oppProfileButton)
            }
        }else{
            itemBinding.ownChatting.visibility = GONE
            itemBinding.oppChatting.visibility = VISIBLE
            itemBinding.oppChattingText.text = datas[position].message.toString()
            itemBinding.oppTimeText.text = datas[position].time.toString()
            itemBinding.oppWriterText.text = "탈퇴한 회원입니다."
        }

    }

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {}

}
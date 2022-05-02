package com.ssafy.family.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.family.data.Event
import com.ssafy.family.databinding.ItemScheduleBinding

class ScheduleAdapter(val context: Context, val onClick: (Event) -> Unit) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    val events = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ScheduleViewHolder(
            ItemScheduleBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ScheduleViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class ScheduleViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition])
            }
        }

        fun bind(event: Event) {
            binding.itemEventText.text = event.text
        }
    }
}

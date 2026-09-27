package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.streamapp.R
import com.streamapp.utils.LiveTvChannel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ChannelAdapter(
    private val onItemClick: (LiveTvChannel) -> Unit
) : ListAdapter<LiveTvChannel, ChannelAdapter.ChannelViewHolder>(ChannelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return ChannelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChannelViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(channel: LiveTvChannel) {
            val tvChannelName = itemView.findViewById<TextView>(R.id.tvChannelName)
            val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)
            val tvCountry = itemView.findViewById<TextView>(R.id.tvCountry)
            val imgChannel = itemView.findViewById<ImageView>(R.id.imgChannel)

            tvChannelName.text = channel.name
            tvCategory.text = channel.category
            tvCountry.text = channel.country

            Glide.with(itemView.context)
                .load(channel.logo)
                .transform(CenterCrop(), RoundedCorners(8))
                .placeholder(R.drawable.shimmer_item)
                .error(R.drawable.shimmer_item)
                .into(imgChannel)

            itemView.setOnClickListener { onItemClick(channel) }
        }
    }

    class ChannelDiffCallback : DiffUtil.ItemCallback<LiveTvChannel>() {
        override fun areItemsTheSame(oldItem: LiveTvChannel, newItem: LiveTvChannel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LiveTvChannel, newItem: LiveTvChannel) = oldItem == newItem
    }
}

package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.streamapp.R
import com.streamapp.databinding.ItemChannelBinding
import com.streamapp.utils.LiveTvChannel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ChannelAdapter(
    private val onItemClick: (LiveTvChannel) -> Unit
) : ListAdapter<LiveTvChannel, ChannelAdapter.ChannelViewHolder>(ChannelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val binding = ItemChannelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChannelViewHolder(private val binding: ItemChannelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(channel: LiveTvChannel) {
            binding.tvChannelName.text = channel.name
            binding.tvCategory.text = channel.category
            binding.tvCountry.text = channel.country

            Glide.with(binding.root.context)
                .load(channel.logo)
                .transform(CenterCrop(), RoundedCorners(8))
                .placeholder(R.drawable.shimmer_item)
                .error(R.drawable.shimmer_item)
                .into(binding.imgChannel)

            binding.root.setOnClickListener { onItemClick(channel) }
        }
    }

    class ChannelDiffCallback : DiffUtil.ItemCallback<LiveTvChannel>() {
        override fun areItemsTheSame(oldItem: LiveTvChannel, newItem: LiveTvChannel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LiveTvChannel, newItem: LiveTvChannel) = oldItem == newItem
    }
}

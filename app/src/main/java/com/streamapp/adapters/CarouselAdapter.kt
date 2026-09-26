package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.streamapp.R
import com.streamapp.api.TMDBSeriesItem
import com.streamapp.databinding.ItemCarouselBinding

class CarouselAdapter(
    private val onItemClick: (TMDBSeriesItem) -> Unit
) : ListAdapter<TMDBSeriesItem, CarouselAdapter.CarouselViewHolder>(CarouselDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarouselViewHolder(private val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TMDBSeriesItem) {
            binding.tvTitle.text = item.name

            Glide.with(binding.root.context)
                .load(item.getPosterUrl())
                .transform(CenterCrop(), RoundedCorners(8))
                .placeholder(R.drawable.shimmer_item)
                .error(R.drawable.shimmer_item)
                .into(binding.imgPoster)

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class CarouselDiffCallback : DiffUtil.ItemCallback<TMDBSeriesItem>() {
        override fun areItemsTheSame(oldItem: TMDBSeriesItem, newItem: TMDBSeriesItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TMDBSeriesItem, newItem: TMDBSeriesItem) = oldItem == newItem
    }
}

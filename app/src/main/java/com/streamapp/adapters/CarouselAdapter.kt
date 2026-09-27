package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.streamapp.R
import com.streamapp.api.TMDBSeriesItem

class CarouselAdapter(
    private val onItemClick: (TMDBSeriesItem) -> Unit
) : ListAdapter<TMDBSeriesItem, CarouselAdapter.CarouselViewHolder>(CarouselDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarouselViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(item: TMDBSeriesItem) {
            val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            val imgPoster = itemView.findViewById<ImageView>(R.id.imgPoster)

            tvTitle.text = item.name

            Glide.with(itemView.context)
                .load(item.getPosterUrl())
                .transform(CenterCrop(), RoundedCorners(8))
                .placeholder(R.drawable.shimmer_item)
                .error(R.drawable.shimmer_item)
                .into(imgPoster)

            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    class CarouselDiffCallback : DiffUtil.ItemCallback<TMDBSeriesItem>() {
        override fun areItemsTheSame(oldItem: TMDBSeriesItem, newItem: TMDBSeriesItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TMDBSeriesItem, newItem: TMDBSeriesItem) = oldItem == newItem
    }
}

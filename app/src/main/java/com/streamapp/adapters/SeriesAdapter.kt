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
import com.streamapp.databinding.ItemMovieBinding

class SeriesAdapter(
    private val onItemClick: (TMDBSeriesItem) -> Unit
) : ListAdapter<TMDBSeriesItem, SeriesAdapter.SeriesViewHolder>(SeriesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SeriesViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(series: TMDBSeriesItem) {
            binding.tvTitle.text = series.name
            binding.tvInfo.text = buildString {
                append(series.getYear())
                if (series.getRating() != "0.0") append(" | ${series.getRating()}")
            }

            Glide.with(binding.root.context)
                .load(series.getPosterUrl())
                .transform(CenterCrop(), RoundedCorners(16))
                .placeholder(R.drawable.shimmer_item)
                .error(R.drawable.shimmer_item)
                .into(binding.imgPoster)

            if (series.voteAverage >= 7.0) {
                binding.tvQuality.text = "HD"
                binding.tvQuality.visibility = android.view.View.VISIBLE
            } else if (series.voteAverage >= 5.0) {
                binding.tvQuality.text = "SD"
                binding.tvQuality.visibility = android.view.View.VISIBLE
            } else {
                binding.tvQuality.visibility = android.view.View.GONE
            }

            binding.root.setOnClickListener { onItemClick(series) }
        }
    }

    class SeriesDiffCallback : DiffUtil.ItemCallback<TMDBSeriesItem>() {
        override fun areItemsTheSame(oldItem: TMDBSeriesItem, newItem: TMDBSeriesItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TMDBSeriesItem, newItem: TMDBSeriesItem) = oldItem == newItem
    }
}

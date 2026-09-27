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

class SeriesAdapter(
    private val onItemClick: (TMDBSeriesItem) -> Unit
) : ListAdapter<TMDBSeriesItem, SeriesAdapter.SeriesViewHolder>(SeriesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SeriesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(series: TMDBSeriesItem) {
            val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            val tvInfo = itemView.findViewById<TextView>(R.id.tvInfo)
            val imgPoster = itemView.findViewById<ImageView>(R.id.imgPoster)
            val tvQuality = itemView.findViewById<TextView>(R.id.tvQuality)

            tvTitle.text = series.name
            tvInfo.text = buildString {
                append(series.getYear())
                if (series.getRating() != "0.0") append(" | ${series.getRating()}")
            }

            Glide.with(itemView.context)
                .load(series.getPosterUrl())
                .transform(CenterCrop(), RoundedCorners(16))
                .placeholder(R.drawable.shimmer_item)
                .error(R.drawable.shimmer_item)
                .into(imgPoster)

            if (series.voteAverage >= 7.0) {
                tvQuality.text = "HD"
                tvQuality.visibility = View.VISIBLE
            } else if (series.voteAverage >= 5.0) {
                tvQuality.text = "SD"
                tvQuality.visibility = View.VISIBLE
            } else {
                tvQuality.visibility = View.GONE
            }

            itemView.setOnClickListener { onItemClick(series) }
        }
    }

    class SeriesDiffCallback : DiffUtil.ItemCallback<TMDBSeriesItem>() {
        override fun areItemsTheSame(oldItem: TMDBSeriesItem, newItem: TMDBSeriesItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TMDBSeriesItem, newItem: TMDBSeriesItem) = oldItem == newItem
    }
}

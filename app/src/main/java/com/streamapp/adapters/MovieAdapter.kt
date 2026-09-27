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
import com.streamapp.api.TMDBMovieItem

class MovieAdapter(
    private val onItemClick: (TMDBMovieItem) -> Unit
) : ListAdapter<TMDBMovieItem, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(movie: TMDBMovieItem) {
            val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            val tvInfo = itemView.findViewById<TextView>(R.id.tvInfo)
            val imgPoster = itemView.findViewById<ImageView>(R.id.imgPoster)
            val tvQuality = itemView.findViewById<TextView>(R.id.tvQuality)

            tvTitle.text = movie.title
            tvInfo.text = buildString {
                append(movie.getYear())
                if (movie.getRating() != "0.0") append(" | ${movie.getRating()}")
            }

            Glide.with(itemView.context)
                .load(movie.getPosterUrl())
                .transform(CenterCrop(), RoundedCorners(16))
                .placeholder(R.drawable.shimmer_item)
                .error(R.drawable.shimmer_item)
                .into(imgPoster)

            if (movie.voteAverage >= 7.0) {
                tvQuality.text = "HD"
                tvQuality.visibility = View.VISIBLE
            } else if (movie.voteAverage >= 5.0) {
                tvQuality.text = "SD"
                tvQuality.visibility = View.VISIBLE
            } else {
                tvQuality.visibility = View.GONE
            }

            itemView.setOnClickListener { onItemClick(movie) }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<TMDBMovieItem>() {
        override fun areItemsTheSame(oldItem: TMDBMovieItem, newItem: TMDBMovieItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TMDBMovieItem, newItem: TMDBMovieItem) = oldItem == newItem
    }
}

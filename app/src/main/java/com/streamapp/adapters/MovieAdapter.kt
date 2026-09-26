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
import com.streamapp.api.TMDBMovieItem
import com.streamapp.databinding.ItemMovieBinding

class MovieAdapter(
    private val onItemClick: (TMDBMovieItem) -> Unit
) : ListAdapter<TMDBMovieItem, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: TMDBMovieItem) {
            binding.tvTitle.text = movie.title
            binding.tvInfo.text = buildString {
                append(movie.getYear())
                if (movie.getRating() != "0.0") append(" | ${movie.getRating()}")
            }

            Glide.with(binding.root.context)
                .load(movie.getPosterUrl())
                .transform(CenterCrop(), RoundedCorners(16))
                .placeholder(R.drawable.shimmer_item)
                .error(R.drawable.shimmer_item)
                .into(binding.imgPoster)

            if (movie.voteAverage >= 7.0) {
                binding.tvQuality.text = "HD"
                binding.tvQuality.visibility = android.view.View.VISIBLE
            } else if (movie.voteAverage >= 5.0) {
                binding.tvQuality.text = "SD"
                binding.tvQuality.visibility = android.view.View.VISIBLE
            } else {
                binding.tvQuality.visibility = android.view.View.GONE
            }

            binding.root.setOnClickListener { onItemClick(movie) }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<TMDBMovieItem>() {
        override fun areItemsTheSame(oldItem: TMDBMovieItem, newItem: TMDBMovieItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TMDBMovieItem, newItem: TMDBMovieItem) = oldItem == newItem
    }
}

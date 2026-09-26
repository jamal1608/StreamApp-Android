package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.streamapp.api.TMDBEpisode
import com.streamapp.databinding.ItemEpisodeBinding

class EpisodeAdapter : ListAdapter<TMDBEpisode, EpisodeAdapter.EpisodeViewHolder>(EpisodeDiffCallback()) {

    private var onEpisodeClick: ((Int) -> Unit)? = null

    fun setEpisodes(episodes: List<TMDBEpisode>) {
        submitList(episodes.toList())
    }

    fun setOnEpisodeClickListener(listener: (Int) -> Unit) {
        onEpisodeClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EpisodeViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: TMDBEpisode) {
            binding.tvEpNumber.text = episode.episodeNumber.toString()
            binding.tvEpTitle.text = episode.name
            binding.tvEpInfo.text = buildString {
                if (episode.runtime > 0) append("${episode.runtime}m")
                if (episode.airDate.isNotEmpty()) {
                    if (isNotEmpty()) append(" | ")
                    append(episode.airDate)
                }
            }
            binding.root.setOnClickListener {
                onEpisodeClick?.invoke(episode.episodeNumber)
            }
        }
    }

    class EpisodeDiffCallback : DiffUtil.ItemCallback<TMDBEpisode>() {
        override fun areItemsTheSame(oldItem: TMDBEpisode, newItem: TMDBEpisode) = oldItem.episodeNumber == newItem.episodeNumber
        override fun areContentsTheSame(oldItem: TMDBEpisode, newItem: TMDBEpisode) = oldItem == newItem
    }
}

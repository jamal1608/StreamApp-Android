package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.streamapp.R
import com.streamapp.api.TMDBEpisode

class EpisodeAdapter : ListAdapter<TMDBEpisode, EpisodeAdapter.EpisodeViewHolder>(EpisodeDiffCallback()) {

    private var onEpisodeClick: ((Int) -> Unit)? = null

    fun setEpisodes(episodes: List<TMDBEpisode>) {
        submitList(episodes.toList())
    }

    fun setOnEpisodeClickListener(listener: (Int) -> Unit) {
        onEpisodeClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EpisodeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(episode: TMDBEpisode) {
            val tvEpNumber = itemView.findViewById<TextView>(R.id.tvEpNumber)
            val tvEpTitle = itemView.findViewById<TextView>(R.id.tvEpTitle)
            val tvEpInfo = itemView.findViewById<TextView>(R.id.tvEpInfo)

            tvEpNumber.text = episode.episodeNumber.toString()
            tvEpTitle.text = episode.name
            tvEpInfo.text = buildString {
                if (episode.runtime > 0) append("${episode.runtime}m")
                if (episode.airDate.isNotEmpty()) {
                    if (isNotEmpty()) append(" | ")
                    append(episode.airDate)
                }
            }
            itemView.setOnClickListener {
                onEpisodeClick?.invoke(episode.episodeNumber)
            }
        }
    }

    class EpisodeDiffCallback : DiffUtil.ItemCallback<TMDBEpisode>() {
        override fun areItemsTheSame(oldItem: TMDBEpisode, newItem: TMDBEpisode) = oldItem.episodeNumber == newItem.episodeNumber
        override fun areContentsTheSame(oldItem: TMDBEpisode, newItem: TMDBEpisode) = oldItem == newItem
    }
}

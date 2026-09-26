package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.streamapp.api.TMDBSeasonDetail
import com.streamapp.databinding.ItemSeasonBinding

class SeasonAdapter : RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {

    private val seasons = mutableListOf<TMDBSeasonDetail>()
    private var onEpisodeClick: ((String, Int, Int) -> Unit)? = null

    fun setSeasons(newSeasons: List<TMDBSeasonDetail>) {
        seasons.clear()
        seasons.addAll(newSeasons)
        notifyDataSetChanged()
    }

    fun setOnEpisodeClickListener(listener: (String, Int, Int) -> Unit) {
        onEpisodeClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        val binding = ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.bind(seasons[position])
    }

    override fun getItemCount() = seasons.size

    inner class SeasonViewHolder(private val binding: ItemSeasonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(season: TMDBSeasonDetail) {
            binding.tvSeasonTitle.text = season.name.ifEmpty { "Season ${season.seasonNumber}" }

            val episodeAdapter = EpisodeAdapter()
            episodeAdapter.setEpisodes(season.episodes)
            episodeAdapter.setOnEpisodeClickListener { epNum ->
                onEpisodeClick?.invoke(season.seasonNumber.toString(), season.seasonNumber, epNum)
            }
            binding.rvEpisodes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = episodeAdapter
                isNestedScrollingEnabled = false
            }
        }
    }
}

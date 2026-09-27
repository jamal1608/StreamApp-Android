package com.streamapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.streamapp.R
import com.streamapp.api.TMDBSeasonDetail

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_season, parent, false)
        return SeasonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.bind(seasons[position])
    }

    override fun getItemCount() = seasons.size

    inner class SeasonViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(season: TMDBSeasonDetail) {
            val tvSeasonTitle = itemView.findViewById<TextView>(R.id.tvSeasonTitle)
            val rvEpisodes = itemView.findViewById<RecyclerView>(R.id.rvEpisodes)

            tvSeasonTitle.text = season.name.ifEmpty { "Season ${season.seasonNumber}" }

            val episodeAdapter = EpisodeAdapter()
            episodeAdapter.setEpisodes(season.episodes)
            episodeAdapter.setOnEpisodeClickListener { epNum ->
                onEpisodeClick?.invoke(season.seasonNumber.toString(), season.seasonNumber, epNum)
            }
            rvEpisodes.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = episodeAdapter
                isNestedScrollingEnabled = false
            }
        }
    }
}

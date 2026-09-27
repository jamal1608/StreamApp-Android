package com.streamapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.streamapp.R
import com.streamapp.adapters.SeasonAdapter
import com.streamapp.api.FreeRetrofitClient
import com.streamapp.api.TMDBMovieDetail
import com.streamapp.api.TMDBSeriesDetail
import com.streamapp.ui.player.PlayerActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var imgBackdrop: ImageView
    private lateinit var imgPoster: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvMeta: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvCategory: TextView
    private lateinit var btnWatch: ImageButton
    private lateinit var rvEpisodes: RecyclerView
    private lateinit var adView: LinearLayout

    private lateinit var seasonAdapter: SeasonAdapter
    private var contentId: String = ""
    private var contentType: String = "movie"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        toolbar = findViewById(R.id.toolbar)
        imgBackdrop = findViewById(R.id.imgBackdrop)
        imgPoster = findViewById(R.id.imgPoster)
        tvTitle = findViewById(R.id.tvTitle)
        tvMeta = findViewById(R.id.tvMeta)
        tvDescription = findViewById(R.id.tvDescription)
        tvRating = findViewById(R.id.tvRating)
        tvYear = findViewById(R.id.tvYear)
        tvCategory = findViewById(R.id.tvCategory)
        btnWatch = findViewById(R.id.btnWatch)
        rvEpisodes = findViewById(R.id.rvEpisodes)
        adView = findViewById(R.id.adView)

        contentId = intent.getStringExtra("id") ?: ""
        contentType = intent.getStringExtra("type") ?: "movie"

        setupToolbar()
        setupSeasonAdapter()
        setupAd()
        loadDetails()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupSeasonAdapter() {
        seasonAdapter = SeasonAdapter()
        rvEpisodes.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = seasonAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupAd() {
        val adBanner = AdView(this)
        adBanner.adSize = AdSize.BANNER
        adBanner.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adRequest = AdRequest.Builder().build()
        adBanner.loadAd(adRequest)
        this.adView.removeAllViews()
        this.adView.addView(adBanner)
    }

    private fun loadDetails() {
        lifecycleScope.launch {
            try {
                val api = FreeRetrofitClient.api
                if (contentType == "movie") {
                    val response = api.getMovieDetails(contentId)
                    if (response.isSuccessful) {
                        response.body()?.let { displayMovieDetails(it) }
                    }
                } else {
                    val response = api.getSeriesDetails(contentId)
                    if (response.isSuccessful) {
                        response.body()?.let { displaySeriesDetails(it) }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayMovieDetails(movie: TMDBMovieDetail) {
        tvTitle.text = movie.title
        tvDescription.text = movie.overview
        tvMeta.text = "${movie.getYear()} | ${movie.getGenresString()}"
        tvRating.text = "Rating: ${movie.getRating()}"
        tvYear.text = "Year: ${movie.getYear()}"
        tvCategory.text = movie.getGenresString()

        Glide.with(this).load(movie.getBackdropUrl()).transform(CenterCrop())
            .placeholder(R.drawable.shimmer_item).into(imgBackdrop)
        Glide.with(this).load(movie.getPosterUrl()).transform(CenterCrop(), RoundedCorners(16))
            .placeholder(R.drawable.shimmer_item).into(imgPoster)

        btnWatch.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("title", movie.title)
            intent.putExtra("id", movie.id.toString())
            intent.putExtra("type", "movie")
            startActivity(intent)
        }
    }

    private fun displaySeriesDetails(series: TMDBSeriesDetail) {
        tvTitle.text = series.name
        tvDescription.text = series.overview
        tvMeta.text = "${series.getYear()} | ${series.getGenresString()}"
        tvRating.text = "Rating: ${series.getRating()}"
        tvYear.text = "Year: ${series.getYear()}"
        tvCategory.text = series.getGenresString()

        Glide.with(this).load(series.getBackdropUrl()).transform(CenterCrop())
            .placeholder(R.drawable.shimmer_item).into(imgBackdrop)
        Glide.with(this).load(series.getPosterUrl()).transform(CenterCrop(), RoundedCorners(16))
            .placeholder(R.drawable.shimmer_item).into(imgPoster)

        if (series.seasons.isNotEmpty()) {
            rvEpisodes.visibility = View.VISIBLE
            loadSeasons(series.id, series.seasons.size)
        }

        btnWatch.setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("title", series.name)
            intent.putExtra("id", series.id.toString())
            intent.putExtra("type", "series")
            startActivity(intent)
        }
    }

    private fun loadSeasons(seriesId: Int, seasonCount: Int) {
        lifecycleScope.launch {
            try {
                val seasonDetails = mutableListOf<com.streamapp.api.TMDBSeasonDetail>()
                for (i in 1..seasonCount) {
                    val response = FreeRetrofitClient.api.getSeasonDetails(seriesId.toString(), i)
                    if (response.isSuccessful) {
                        response.body()?.let { seasonDetails.add(it) }
                    }
                }
                seasonAdapter.setSeasons(seasonDetails)
                seasonAdapter.setOnEpisodeClickListener { _, season, episode ->
                    val intent = Intent(this@DetailActivity, PlayerActivity::class.java)
                    intent.putExtra("title", "Season $season - Episode $episode")
                    intent.putExtra("id", seriesId.toString())
                    intent.putExtra("type", "series")
                    intent.putExtra("season", season)
                    intent.putExtra("episode", episode)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

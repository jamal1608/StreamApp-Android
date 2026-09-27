package com.streamapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.streamapp.R
import com.streamapp.adapters.CarouselAdapter
import com.streamapp.adapters.MovieAdapter
import com.streamapp.ui.detail.DetailActivity
import com.streamapp.viewmodels.HomeViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var popularMoviesAdapter: MovieAdapter
    private lateinit var trendingAdapter: MovieAdapter
    private lateinit var seriesAdapter: CarouselAdapter

    private lateinit var rvPopularMovies: RecyclerView
    private lateinit var rvTrending: RecyclerView
    private lateinit var rvPopularSeries: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var adContainer: FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPopularMovies = view.findViewById(R.id.rvPopularMovies)
        rvTrending = view.findViewById(R.id.rvTrending)
        rvPopularSeries = view.findViewById(R.id.rvPopularSeries)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        adContainer = view.findViewById(R.id.adContainer)

        setupAdapters()
        setupRecyclerViews()
        setupSwipeRefresh()
        setupAd()
        observeViewModel()
        viewModel.loadData()
    }

    private fun setupAdapters() {
        popularMoviesAdapter = MovieAdapter { item ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", item.id.toString())
            intent.putExtra("type", "movie")
            startActivity(intent)
        }

        trendingAdapter = MovieAdapter { item ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", item.id.toString())
            intent.putExtra("type", "movie")
            startActivity(intent)
        }

        seriesAdapter = CarouselAdapter { item ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", item.id.toString())
            intent.putExtra("type", "series")
            startActivity(intent)
        }
    }

    private fun setupRecyclerViews() {
        rvPopularMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }

        rvTrending.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = trendingAdapter
        }

        rvPopularSeries.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = seriesAdapter
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            viewModel.loadData()
        }
        swipeRefresh.setColorSchemeResources(
            com.streamapp.R.color.primary,
            com.streamapp.R.color.secondary
        )
    }

    private fun setupAd() {
        val adView = AdView(requireContext())
        adView.adSize = AdSize.BANNER
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test ad unit
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adContainer.removeAllViews()
        adContainer.addView(adView)
    }

    private fun observeViewModel() {
        viewModel.popularMovies.observe(viewLifecycleOwner) { movies ->
            popularMoviesAdapter.submitList(movies)
        }

        viewModel.trendingMovies.observe(viewLifecycleOwner) { movies ->
            trendingAdapter.submitList(movies.take(10))
        }

        viewModel.popularSeries.observe(viewLifecycleOwner) { series ->
            seriesAdapter.submitList(series)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

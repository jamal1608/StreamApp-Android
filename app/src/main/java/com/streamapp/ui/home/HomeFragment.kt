package com.streamapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.streamapp.adapters.CarouselAdapter
import com.streamapp.adapters.MovieAdapter
import com.streamapp.databinding.FragmentHomeBinding
import com.streamapp.ui.detail.DetailActivity
import com.streamapp.viewmodels.HomeViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var popularMoviesAdapter: MovieAdapter
    private lateinit var trendingAdapter: MovieAdapter
    private lateinit var seriesAdapter: CarouselAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.rvPopularMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }

        binding.rvTrending.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = trendingAdapter
        }

        binding.rvPopularSeries.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = seriesAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadData()
        }
        binding.swipeRefresh.setColorSchemeResources(
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

        binding.adContainer.removeAllViews()
        binding.adContainer.addView(adView)
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
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

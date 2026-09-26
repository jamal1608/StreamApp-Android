package com.streamapp.ui.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.streamapp.adapters.MovieAdapter
import com.streamapp.api.RetrofitClient
import com.streamapp.databinding.FragmentContentListBinding
import com.streamapp.ui.detail.DetailActivity
import com.streamapp.viewmodels.MoviesViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize

class MoviesFragment : Fragment() {

    private var _binding: FragmentContentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    private val categories = listOf("Popular", "Now Playing", "Top Rated")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategories()
        setupRecyclerView()
        setupSearch()
        setupSwipeRefresh()
        setupAd()
        observeViewModel()
        viewModel.loadMovies()
    }

    private fun setupCategories() {
        val chipGroup = binding.chipGroupCategories
        categories.forEachIndexed { index, category ->
            val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                text = category
                isCheckable = true
                isChecked = index == 0
                setOnClickListener {
                    viewModel.loadMovies(category, refresh = true)
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter { item ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", item.id.toString())
            intent.putExtra("type", "movie")
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@MoviesFragment.adapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    if (lastVisibleItem >= totalItemCount - 5) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    private fun setupSearch() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchMovies(query)
                } else {
                    viewModel.loadMovies(refresh = true)
                }
                true
            } else false
        }

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchMovies(query)
            } else {
                viewModel.loadMovies(refresh = true)
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadMovies(refresh = true)
        }
    }

    private fun setupAd() {
        val adView = com.google.android.gms.ads.AdView(requireContext())
        adView.adSize = AdSize.BANNER
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        binding.adView.removeAllViews()
        binding.adView.addView(adView)
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
            binding.tvEmpty.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
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

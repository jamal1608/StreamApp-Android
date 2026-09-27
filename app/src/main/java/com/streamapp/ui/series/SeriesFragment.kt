package com.streamapp.ui.series

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.ChipGroup
import com.streamapp.R
import com.streamapp.adapters.SeriesAdapter
import com.streamapp.ui.detail.DetailActivity
import com.streamapp.viewmodels.SeriesViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize

class SeriesFragment : Fragment() {

    private val viewModel: SeriesViewModel by viewModels()
    private lateinit var adapter: SeriesAdapter

    private lateinit var chipGroupCategories: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: ImageButton
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var adView: LinearLayout
    private lateinit var tvEmpty: TextView

    private val categories = listOf("Popular", "Airing Today", "Top Rated")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_content_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroupCategories = view.findViewById(R.id.chipGroupCategories)
        recyclerView = view.findViewById(R.id.recyclerView)
        etSearch = view.findViewById(R.id.etSearch)
        btnSearch = view.findViewById(R.id.btnSearch)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        adView = view.findViewById(R.id.adView)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        setupCategories()
        setupRecyclerView()
        setupSearch()
        setupSwipeRefresh()
        setupAd()
        observeViewModel()
        viewModel.loadSeries()
    }

    private fun setupCategories() {
        categories.forEachIndexed { index, category ->
            val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                text = category
                isCheckable = true
                isChecked = index == 0
                setOnClickListener {
                    viewModel.loadSeries(category, refresh = true)
                }
            }
            chipGroupCategories.addView(chip)
        }
    }

    private fun setupRecyclerView() {
        adapter = SeriesAdapter { item ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("id", item.id.toString())
            intent.putExtra("type", "series")
            startActivity(intent)
        }

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@SeriesFragment.adapter

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
        etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchSeries(query)
                } else {
                    viewModel.loadSeries(refresh = true)
                }
                true
            } else false
        }

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchSeries(query)
            } else {
                viewModel.loadSeries(refresh = true)
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            viewModel.loadSeries(refresh = true)
        }
    }

    private fun setupAd() {
        val adBanner = com.google.android.gms.ads.AdView(requireContext())
        adBanner.adSize = AdSize.BANNER
        adBanner.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adRequest = AdRequest.Builder().build()
        adBanner.loadAd(adRequest)

        this.adView.removeAllViews()
        this.adView.addView(adBanner)
    }

    private fun observeViewModel() {
        viewModel.series.observe(viewLifecycleOwner) { series ->
            adapter.submitList(series)
            tvEmpty.visibility = if (series.isEmpty()) View.VISIBLE else View.GONE
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

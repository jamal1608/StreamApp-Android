package com.streamapp.ui.livetv

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.ChipGroup
import com.streamapp.R
import com.streamapp.adapters.ChannelAdapter
import com.streamapp.ui.player.PlayerActivity
import com.streamapp.viewmodels.LiveTvViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize

class LiveTvFragment : Fragment() {

    private val viewModel: LiveTvViewModel by viewModels()
    private lateinit var adapter: ChannelAdapter

    private lateinit var chipGroupCategories: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var adView: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_live_tv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroupCategories = view.findViewById(R.id.chipGroupCategories)
        recyclerView = view.findViewById(R.id.recyclerView)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        adView = view.findViewById(R.id.adView)

        setupRecyclerView()
        setupSwipeRefresh()
        setupAd()
        observeViewModel()
        viewModel.loadChannels()
    }

    private fun setupRecyclerView() {
        adapter = ChannelAdapter { channel ->
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra("url", channel.streamUrl)
            intent.putExtra("title", channel.name)
            startActivity(intent)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@LiveTvFragment.adapter
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            viewModel.loadChannels()
        }
    }

    private fun setupAd() {
        val adView = com.google.android.gms.ads.AdView(requireContext())
        adView.adSize = AdSize.BANNER
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        this.adView.removeAllViews()
        this.adView.addView(adView)
    }

    private fun setupCategories() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            chipGroupCategories.removeAllViews()
            categories.forEach { category ->
                val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                    text = category
                    isCheckable = true
                    setOnClickListener {
                        viewModel.filterByCategory(category)
                    }
                }
                chipGroupCategories.addView(chip)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.filteredChannels.observe(viewLifecycleOwner) { channels ->
            adapter.submitList(channels)
        }

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            setupCategories()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            swipeRefresh.isRefreshing = isLoading
        }
    }
}

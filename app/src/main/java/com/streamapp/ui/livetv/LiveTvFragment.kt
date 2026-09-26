package com.streamapp.ui.livetv

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.streamapp.adapters.ChannelAdapter
import com.streamapp.databinding.FragmentLiveTvBinding
import com.streamapp.ui.player.PlayerActivity
import com.streamapp.viewmodels.LiveTvViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize

class LiveTvFragment : Fragment() {

    private var _binding: FragmentLiveTvBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LiveTvViewModel by viewModels()
    private lateinit var adapter: ChannelAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLiveTvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@LiveTvFragment.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadChannels()
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

    private fun setupCategories() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            val chipGroup = binding.chipGroupCategories
            chipGroup.removeAllViews()
            categories.forEach { category ->
                val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                    text = category
                    isCheckable = true
                    setOnClickListener {
                        viewModel.filterByCategory(category)
                    }
                }
                chipGroup.addView(chip)
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
            binding.swipeRefresh.isRefreshing = isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

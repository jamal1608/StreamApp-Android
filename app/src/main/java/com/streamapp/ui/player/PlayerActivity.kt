package com.streamapp.ui.player

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.streamapp.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null
    private var videoUrl: String = ""
    private var videoTitle: String = ""
    private val streamUrls = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoUrl = intent.getStringExtra("url") ?: ""
        videoTitle = intent.getStringExtra("title") ?: ""

        binding.tvTitle.text = videoTitle
        setupAd()
        setupServers()
        initializePlayer()
    }

    private fun setupAd() {
        val adView = com.google.android.gms.ads.AdView(this)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        binding.adView.removeAllViews()
        binding.adView.addView(adView)
    }

    private fun setupServers() {
        streamUrls.clear()

        if (videoUrl.isNotEmpty()) {
            streamUrls.add(videoUrl)
        }

        val serverNames = listOf("Server 1", "Server 2", "Server 3", "Server 4", "Server 5")

        for (i in 0 until minOf(5, maxOf(streamUrls.size, 3))) {
            val chip = com.google.android.material.chip.Chip(this).apply {
                text = if (i < streamUrls.size) serverNames[i] else serverNames[i]
                isCheckable = true
                isChecked = i == 0
                setOnClickListener {
                    if (i < streamUrls.size && streamUrls[i].isNotEmpty()) {
                        playVideo(streamUrls[i])
                    } else {
                        Toast.makeText(this@PlayerActivity, "Stream not available", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.chipGroupServers.addView(chip)
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    Toast.makeText(this@PlayerActivity, "Playback error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

            if (videoUrl.isNotEmpty()) {
                playVideo(videoUrl)
            }
        }
    }

    private fun playVideo(url: String) {
        player?.let { exoPlayer ->
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    override fun onStart() {
        super.onStart()
        player?.playWhenReady = true
    }

    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}

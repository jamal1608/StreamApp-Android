package com.streamapp.ui.player

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.android.material.chip.ChipGroup
import com.streamapp.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize

class PlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private var videoUrl: String = ""
    private var videoTitle: String = ""
    private val streamUrls = mutableListOf<String>()

    private lateinit var tvTitle: TextView
    private lateinit var chipGroupServers: ChipGroup
    private lateinit var playerView: PlayerView
    private lateinit var adView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        tvTitle = findViewById(R.id.tvTitle)
        chipGroupServers = findViewById(R.id.chipGroupServers)
        playerView = findViewById(R.id.playerView)
        adView = findViewById(R.id.adView)

        videoUrl = intent.getStringExtra("url") ?: ""
        videoTitle = intent.getStringExtra("title") ?: ""

        tvTitle.text = videoTitle
        setupAd()
        setupServers()
        initializePlayer()
    }

    private fun setupAd() {
        val adBanner = com.google.android.gms.ads.AdView(this)
        adBanner.adSize = AdSize.BANNER
        adBanner.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adRequest = AdRequest.Builder().build()
        adBanner.loadAd(adRequest)

        this.adView.removeAllViews()
        this.adView.addView(adBanner)
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
            chipGroupServers.addView(chip)
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView.player = exoPlayer

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

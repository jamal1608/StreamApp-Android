package com.streamapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.streamapp.R
import com.streamapp.databinding.ActivityMainBinding
import com.streamapp.ui.livetv.LiveTvFragment
import com.streamapp.ui.movies.MoviesFragment
import com.streamapp.ui.series.SeriesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    binding.toolbar.title = ""
                    true
                }
                R.id.nav_movies -> {
                    loadFragment(MoviesFragment())
                    binding.toolbar.title = ""
                    true
                }
                R.id.nav_series -> {
                    loadFragment(SeriesFragment())
                    binding.toolbar.title = ""
                    true
                }
                R.id.nav_live_tv -> {
                    loadFragment(LiveTvFragment())
                    binding.toolbar.title = ""
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

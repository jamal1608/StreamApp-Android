package com.streamapp.ui.home

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.streamapp.R
import com.streamapp.ui.livetv.LiveTvFragment
import com.streamapp.ui.movies.MoviesFragment
import com.streamapp.ui.series.SeriesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        bottomNav = findViewById(R.id.bottom_nav)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    toolbar.title = ""
                    true
                }
                R.id.nav_movies -> {
                    loadFragment(MoviesFragment())
                    toolbar.title = ""
                    true
                }
                R.id.nav_series -> {
                    loadFragment(SeriesFragment())
                    toolbar.title = ""
                    true
                }
                R.id.nav_live_tv -> {
                    loadFragment(LiveTvFragment())
                    toolbar.title = ""
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

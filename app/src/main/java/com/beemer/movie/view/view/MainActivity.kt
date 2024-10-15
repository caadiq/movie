package com.beemer.movie.view.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.beemer.movie.R
import com.beemer.movie.databinding.ActivityMainBinding
import com.beemer.movie.viewmodel.MainFragmentType
import com.beemer.movie.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels()

    private var backPressedTime: Long = 0
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity, R.string.str_main_press_back, Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        if (savedInstanceState == null) {
            setupFragment()
        }
        setupBottomNavigation()
        setupViewModel()
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().apply {
            add(binding.containerView.id, HomeFragment(), MainFragmentType.HOME.tag)
            add(binding.containerView.id, ChartFragment(), MainFragmentType.CHART.tag)
            add(binding.containerView.id, SearchFragment(), MainFragmentType.SEARCH.tag)
            add(binding.containerView.id, BookmarkFragment(), MainFragmentType.BOOKMARK.tag)
            commit()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> mainViewModel.setCurrentFragment(0)
                R.id.chart -> mainViewModel.setCurrentFragment(1)
                R.id.search -> mainViewModel.setCurrentFragment(2)
                R.id.bookmark -> mainViewModel.setCurrentFragment(3)
            }
            true
        }
    }

    private fun setupViewModel() {
        mainViewModel.currentFragmentType.observe(this) { fragmentType ->
            val currentFragment = supportFragmentManager.findFragmentByTag(fragmentType.tag)
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                supportFragmentManager.fragments.forEach { fragment ->
                    if (fragment == currentFragment)
                        show(fragment)
                    else
                        hide(fragment)
                }
            }.commit()

            binding.bottomNav.selectedItemId = when (fragmentType) {
                MainFragmentType.HOME -> R.id.home
                MainFragmentType.CHART -> R.id.chart
                MainFragmentType.SEARCH -> R.id.search
                MainFragmentType.BOOKMARK -> R.id.bookmark
                else -> R.id.home
            }
        }
    }
}
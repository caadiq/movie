package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.beemer.movie.databinding.FragmentHomeBinding
import com.beemer.movie.view.adapter.HomePosterAdapter
import com.beemer.movie.view.adapter.MovieReleaseAdapter
import com.beemer.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel by viewModels<MovieViewModel>()

    private val homePosterAdapter = HomePosterAdapter()
    private val recenetMovieReleaseAdapter = MovieReleaseAdapter()
    private val comingMovieReleaseAdapter = MovieReleaseAdapter()

    private var autoScrollJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        autoScrollJob = startAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        autoScrollJob?.cancel()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            autoScrollJob?.cancel()
        } else {
            autoScrollJob = startAutoScroll()
        }
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            adapter = homePosterAdapter

            setCurrentItem(0, false)

            offscreenPageLimit = 5
            isUserInputEnabled = false

            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val transform = CompositePageTransformer()
            transform.addTransformer(MarginPageTransformer(16))
            transform.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.8f + r * 0.2f
            }
            setPageTransformer(transform)
        }

        binding.dotsIndicator.apply {
            dotsClickable = false
            attachTo(binding.viewPager)
        }
    }

    private fun startAutoScroll(): Job {
        return lifecycleScope.launch {
            while (true) {
                delay(5000)
                val itemCount = homePosterAdapter.itemCount
                val currentItem = binding.viewPager.currentItem
                val nextItem = if (currentItem == itemCount - 1) 0 else currentItem + 1
                binding.viewPager.setCurrentItem(nextItem, true)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerRecent.apply {
            adapter = recenetMovieReleaseAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        binding.recyclerComing.apply {
            adapter = comingMovieReleaseAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }
    }

    private fun setupViewModel() {
        movieViewModel.apply {
            getPosterBanner()
            getRecentReleaseList(5)
            getComingReleaseList(5)

            posterBanner.observe(viewLifecycleOwner) { list ->
                homePosterAdapter.setItemList(list)
                setupViewPager()
            }

            recentReleaseList.observe(viewLifecycleOwner) { list ->
                recenetMovieReleaseAdapter.setItemList(list)
            }

            comingReleaseList.observe(viewLifecycleOwner) { list ->
                comingMovieReleaseAdapter.setItemList(list)
            }
        }
    }
}
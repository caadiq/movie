package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.beemer.movie.databinding.FragmentHomeBinding
import com.beemer.movie.view.adapter.HomePosterAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homePosterAdapter = HomePosterAdapter()

    private var autoScrollJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
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
            homePosterAdapter.setItemList(listOf(
                "https://picsum.photos/id/10/400/300",
                "https://picsum.photos/id/20/400/300",
                "https://picsum.photos/id/30/400/300",
                "https://picsum.photos/id/40/400/300",
                "https://picsum.photos/id/50/400/300"
            ))
            setCurrentItem(0, false)

            offscreenPageLimit = 5
            isUserInputEnabled = false
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
}
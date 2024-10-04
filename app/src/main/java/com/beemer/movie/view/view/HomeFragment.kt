package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.beemer.movie.databinding.FragmentHomeBinding
import com.beemer.movie.model.dto.MovieReleaseListDto
import com.beemer.movie.view.adapter.HomePosterAdapter
import com.beemer.movie.view.adapter.MovieReleaseAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        setupViewPager()
        setupRecyclerView()
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
                "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022660.jpg",
                "http://file.koreafilm.or.kr/thm/02/99/18/48/tn_DPK022363.jpg",
                "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022665.jpg",
                "http://file.koreafilm.or.kr/thm/02/00/02/90/tn_DPF007914.JPG",
                "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPA002033.jpg"
            ))
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
        recenetMovieReleaseAdapter.setItemList(
            listOf(
                MovieReleaseListDto("1", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022660.jpg", "베테랑2", "2024.09.13"),
                MovieReleaseListDto("2", "http://file.koreafilm.or.kr/thm/02/99/18/48/tn_DPK022363.jpg", "사랑의 하츄핑", "2024.08.07"),
                MovieReleaseListDto("3", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022665.jpg", "브레드이발소: 빵스타의 탄생", "2024.09.14"),
                MovieReleaseListDto("4", "http://file.koreafilm.or.kr/thm/02/00/02/90/tn_DPF007914.JPG", "비긴 어게인", "2014.08.13"),
                MovieReleaseListDto("5", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPA002033.jpg", "정국: 아이 엠 스틸", "2024.09.18")
            )
        )

        binding.recyclerRecent.apply {
            adapter = recenetMovieReleaseAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        comingMovieReleaseAdapter.setItemList(
            listOf(
                MovieReleaseListDto("6", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022660.jpg", "베테랑2", "2024.09.13"),
                MovieReleaseListDto("7", "http://file.koreafilm.or.kr/thm/02/99/18/48/tn_DPK022363.jpg", "사랑의 하츄핑", "2024.08.07"),
                MovieReleaseListDto("8", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPK022665.jpg", "브레드이발소: 빵스타의 탄생", "2024.09.14"),
                MovieReleaseListDto("9", "http://file.koreafilm.or.kr/thm/02/00/02/90/tn_DPF007914.JPG", "비긴 어게인", "2014.08.13"),
                MovieReleaseListDto("10", "http://file.koreafilm.or.kr/thm/02/99/18/54/tn_DPA002033.jpg", "정국: 아이 엠 스틸", "2024.09.18")
            )
        )

        binding.recyclerComing.apply {
            adapter = comingMovieReleaseAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }
    }
}
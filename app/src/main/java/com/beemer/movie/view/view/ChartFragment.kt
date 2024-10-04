package com.beemer.movie.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.beemer.movie.databinding.FragmentChartBinding
import com.beemer.movie.model.dto.ChartListDto
import com.beemer.movie.view.adapter.ChartAdapter
import com.beemer.movie.view.adapter.ChartItem
import com.beemer.movie.view.utils.DateTimeConverter.convertDate
import com.beemer.movie.viewmodel.ChartTabType
import com.beemer.movie.viewmodel.MainViewModel
import com.beemer.movie.viewmodel.MovieViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val movieViewModel by viewModels<MovieViewModel>()

    private val chartAdapter = ChartAdapter()

    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val yesterday = LocalDate.now().minusDays(1).format(dateFormat)

    private val today = LocalDate.now()
    private val lastMonday = today.minusDays(today.dayOfWeek.value.toLong() + 6).format(dateFormat)
    private val lastSunday = today.minusDays(today.dayOfWeek.value.toLong()).format(dateFormat)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> mainViewModel.setCurrentChartTab(0)
                        1 -> mainViewModel.setCurrentChartTab(1)
                    }
                    true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = chartAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupViewModel() {
        mainViewModel.apply {
            currentChartTabType.observe(viewLifecycleOwner) { chartTabType ->
                when (chartTabType) {
                    ChartTabType.DAILY -> {
                        binding.tabLayout.getTabAt(0)?.select()
                        movieViewModel.getDailyRankList(yesterday)
                    }
                    ChartTabType.WEEKLY -> {
                        binding.tabLayout.getTabAt(1)?.select()
                        movieViewModel.getWeeklyRankList(lastMonday, lastSunday)
                    }
                    else -> {
                        binding.tabLayout.getTabAt(0)?.select()
                        movieViewModel.getDailyRankList(yesterday)
                    }
                }
            }
        }

        movieViewModel.apply {
            dailyRankList.observe(viewLifecycleOwner) { dailyRankList ->
                val chartItems = mutableListOf<ChartItem>()

                chartItems.add(ChartItem.Header("${convertDate(yesterday, "yyyy-MM-dd", "yyyy.MM.dd", Locale.KOREA)} 기준"))
                chartItems.addAll(dailyRankList.map {
                    ChartItem.Chart(
                        ChartListDto(
                            movieCode = it.movieCode,
                            movieName = it.movieName,
                            posterUrl = it.posterUrl,
                            openDate = it.openDate,
                            genre = it.genre,
                            rank = it.rank,
                            rankIncrement = it.rankIncrement
                        )
                    )
                })

                chartAdapter.setItemList(chartItems)
            }

            weeklyRankList.observe(viewLifecycleOwner) { weeklyRankList ->
                val chartItems = mutableListOf<ChartItem>()

                chartItems.add(ChartItem.Header("${convertDate(lastMonday, "yyyy-MM-dd", "yyyy.MM.dd", Locale.KOREA)} ~ ${convertDate(lastSunday, "yyyy-MM-dd", "yyyy.MM.dd", Locale.KOREA)} 기준"))
                chartItems.addAll(weeklyRankList.map {
                    ChartItem.Chart(
                        ChartListDto(
                            movieCode = it.movieCode,
                            movieName = it.movieName,
                            posterUrl = it.posterUrl,
                            openDate = it.openDate,
                            genre = it.genre,
                            rank = it.rank,
                            rankIncrement = it.rankIncrement
                        )
                    )
                })

                chartAdapter.setItemList(chartItems)

            }
        }
    }
}
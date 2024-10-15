package com.beemer.movie.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.beemer.movie.R
import com.beemer.movie.databinding.FragmentChartBinding
import com.beemer.movie.model.dto.ChartListDto
import com.beemer.movie.view.adapter.ChartAdapter
import com.beemer.movie.view.utils.DateTimeConverter.convertDate
import com.beemer.movie.viewmodel.ChartTabType
import com.beemer.movie.viewmodel.MainViewModel
import com.beemer.movie.viewmodel.MovieViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@AndroidEntryPoint
class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel>()
    private val movieViewModel by viewModels<MovieViewModel>()

    private val chartAdapter = ChartAdapter()

    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val today = LocalDate.now()
    private val yesterday = LocalDate.now().minusDays(1).format(dateFormat)
    private val lastMonday = today.minusDays(today.dayOfWeek.value.toLong() + 6).format(dateFormat)
    private val lastSunday = today.minusDays(today.dayOfWeek.value.toLong()).format(dateFormat)

    private var prevDate: String? = null
    private var currentDate: String? = today.format(dateFormat)
    private var nextDate: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupTabLayout()
        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.imgPrev.setOnClickListener {
            prevDate?.let {
                when (mainViewModel.currentChartTabType.value) {
                    ChartTabType.DAILY -> movieViewModel.getDailyRankList(it)
                    ChartTabType.WEEKLY -> {
                        val startDate = LocalDate.parse(it, dateFormat)
                        val endDate = startDate.plusDays(6).format(dateFormat)
                        movieViewModel.getWeeklyRankList(it, endDate)
                    }
                    else -> {}
                }
            }
        }

        binding.imgNext.setOnClickListener {
            nextDate?.let {
                when (mainViewModel.currentChartTabType.value) {
                    ChartTabType.DAILY -> movieViewModel.getDailyRankList(it)
                    ChartTabType.WEEKLY -> {
                        val startDate = LocalDate.parse(it, dateFormat)
                        val endDate = startDate.plusDays(6).format(dateFormat)
                        movieViewModel.getWeeklyRankList(it, endDate)
                    }
                    else -> {}
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            currentDate?.let {
                when (mainViewModel.currentChartTabType.value) {
                    ChartTabType.DAILY -> movieViewModel.getDailyRankList(it)
                    ChartTabType.WEEKLY -> {
                        val startDate = LocalDate.parse(it, dateFormat)
                        val endDate = startDate.plusDays(6).format(dateFormat)
                        movieViewModel.getWeeklyRankList(it, endDate)
                    }
                    else -> {}
                }
            }
        }
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
            itemAnimator = null
        }

        chartAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(requireContext(), DetailsActivity::class.java)
            intent.putExtra("code", item.movieCode)
            startActivity(intent)
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

            prevDate.observe(viewLifecycleOwner) { prevDate ->
                this@ChartFragment.prevDate = prevDate

                binding.imgPrev.apply {
                    if (prevDate == null) {
                        isEnabled = false
                        setColorFilter(ContextCompat.getColor(context, R.color.dark_gray))
                    } else {
                        isEnabled = true
                        setColorFilter(ContextCompat.getColor(context, R.color.light_gray))
                    }
                }
            }

            currentDate.observe(viewLifecycleOwner) { currentDate ->
                this@ChartFragment.currentDate = currentDate
            }

            nextDate.observe(viewLifecycleOwner) { nextDate ->
                this@ChartFragment.nextDate = nextDate

                binding.imgNext.apply {
                    if (nextDate == null) {
                        isEnabled = false
                        setColorFilter(ContextCompat.getColor(context, R.color.dark_gray))
                    } else {
                        isEnabled = true
                        setColorFilter(ContextCompat.getColor(context, R.color.light_gray))
                    }
                }
            }
        }

        movieViewModel.apply {
            dailyRankList.observe(viewLifecycleOwner) { dailyRankList ->
                binding.swipeRefreshLayout.isRefreshing = false

                binding.txtDate.text = convertDate(dailyRankList.date.currenetDate, "yyyy-MM-dd", "MM.dd (E)", Locale.KOREA)

                chartAdapter.setItemList(dailyRankList.rankList.map {
                    ChartListDto(
                        movieCode = it.movieCode,
                        movieName = it.movieName,
                        posterUrl = it.posterUrl,
                        openDate = it.openDate,
                        genre = it.genre,
                        rank = it.rank,
                        rankIncrement = it.rankIncrement
                    )
                })

                if (dailyRankList.rankList.isNotEmpty())
                    binding.recyclerView.scrollToPosition(0)

                mainViewModel.setPrevDate(dailyRankList.date.prevDate)
                mainViewModel.setCurrentDate(dailyRankList.date.currenetDate)
                mainViewModel.setNextDate(dailyRankList.date.nextDate)
            }

            weeklyRankList.observe(viewLifecycleOwner) { weeklyRankList ->
                binding.swipeRefreshLayout.isRefreshing = false

                val startDate = LocalDate.parse(weeklyRankList.date.currenetDate, dateFormat)
                val thursday = startDate.plusDays(3)
                val month = thursday.monthValue
                val weekOfMonth = thursday.get(WeekFields.of(Locale.KOREA).weekOfMonth())

                binding.txtDate.text = "${month}월 ${weekOfMonth}주차"

                chartAdapter.setItemList(weeklyRankList.rankList.map {
                    ChartListDto(
                        movieCode = it.movieCode,
                        movieName = it.movieName,
                        posterUrl = it.posterUrl,
                        openDate = it.openDate,
                        genre = it.genre,
                        rank = it.rank,
                        rankIncrement = it.rankIncrement
                    )
                })

                if (weeklyRankList.rankList.isNotEmpty())
                    binding.recyclerView.scrollToPosition(0)

                mainViewModel.setPrevDate(weeklyRankList.date.prevDate)
                mainViewModel.setCurrentDate(weeklyRankList.date.currenetDate)
                mainViewModel.setNextDate(weeklyRankList.date.nextDate)
            }
        }
    }
}
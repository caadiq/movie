package com.beemer.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class MainFragmentType(val tag: String) {
    HOME("home"),
    CHART("chart"),
    SEARCH("search"),
    BOOKMARK("bookmark")
}
enum class ChartTabType(val tag: String) {
    DAILY("daily"),
    WEEKLY("weekly")
}

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _currentFragmentType = MutableLiveData(MainFragmentType.HOME)
    val currentFragmentType: LiveData<MainFragmentType> = _currentFragmentType

    private val _currentChartTabType = MutableLiveData(ChartTabType.DAILY)
    val currentChartTabType: LiveData<ChartTabType> = _currentChartTabType

    private val _currentDate = MutableLiveData<String?>()
    val currentDate: LiveData<String?> = _currentDate

    private val _prevDate = MutableLiveData<String?>()
    val prevDate: LiveData<String?> = _prevDate

    private val _nextDate = MutableLiveData<String?>()
    val nextDate: LiveData<String?> = _nextDate

    fun setCurrentFragment(item: Int): Boolean {
        val pageType = getPageType(item)
        changeCurrentFragmentType(pageType)

        return true
    }

    private fun getPageType(item: Int): MainFragmentType {
        return when (item) {
            0 -> MainFragmentType.HOME
            1 -> MainFragmentType.CHART
            2 -> MainFragmentType.SEARCH
            3 -> MainFragmentType.BOOKMARK
            else -> MainFragmentType.HOME
        }
    }

    private fun changeCurrentFragmentType(fragmentType: MainFragmentType) {
        if (currentFragmentType.value == fragmentType)
            return

        _currentFragmentType.value = fragmentType
    }

    fun setCurrentChartTab(item: Int): Boolean {
        val tabType = getTabType(item)
        changeCurrentChartTabType(tabType)

        return true
    }

    private fun getTabType(item: Int): ChartTabType {
        return when (item) {
            0 -> ChartTabType.DAILY
            1 -> ChartTabType.WEEKLY
            else -> ChartTabType.DAILY
        }
    }

    private fun changeCurrentChartTabType(tabType: ChartTabType) {
        if (currentChartTabType.value == tabType)
            return

        _currentChartTabType.value = tabType
    }

    fun setCurrentDate(date: String?) {
        _currentDate.value = date
    }

    fun setPrevDate(date: String?) {
        _prevDate.value = date
    }

    fun setNextDate(date: String?) {
        _nextDate.value = date
    }
}
package com.beemer.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.movie.model.dto.PosterBannerDto
import com.beemer.movie.model.dto.RankListDto
import com.beemer.movie.model.dto.ReleaseListDto
import com.beemer.movie.model.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _posterBanner = MutableLiveData<List<PosterBannerDto>>()
    val posterBanner: LiveData<List<PosterBannerDto>> = _posterBanner

    private val _dailyRankList = MutableLiveData<List<RankListDto>>()
    val dailyRankList: LiveData<List<RankListDto>> = _dailyRankList

    private val _weeklyRankList = MutableLiveData<List<RankListDto>>()
    val weeklyRankList: LiveData<List<RankListDto>> = _weeklyRankList

    private val _recentReleaseList = MutableLiveData<List<ReleaseListDto>>()
    val recentReleaseList: LiveData<List<ReleaseListDto>> = _recentReleaseList

    private val _comingReleaseList = MutableLiveData<List<ReleaseListDto>>()
    val comingReleaseList: LiveData<List<ReleaseListDto>> = _comingReleaseList

    fun getPosterBanner() {
        viewModelScope.launch {
            _posterBanner.value = repository.getPosterBanner()
        }
    }

    fun getDailyRankList(date: String) {
        viewModelScope.launch {
            _dailyRankList.value = repository.getDailyRankList(date)
        }
    }

    fun getWeeklyRankList(startDate: String, endDate: String) {
        viewModelScope.launch {
            _weeklyRankList.value = repository.getWeeklyRankList(startDate, endDate)
        }
    }

    fun getRecentReleaseList(limit: Int) {
        viewModelScope.launch {
            _recentReleaseList.value = repository.getRecentReleaseList(limit)
        }
    }

    fun getComingReleaseList(limit: Int) {
        viewModelScope.launch {
            _comingReleaseList.value = repository.getComingReleaseList(limit)
        }
    }
}
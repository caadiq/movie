package com.beemer.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.movie.model.dto.RankListDto
import com.beemer.movie.model.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _dailyRankList = MutableLiveData<List<RankListDto>>()
    val dailyRankList: LiveData<List<RankListDto>> = _dailyRankList

    private val _weeklyRankList = MutableLiveData<List<RankListDto>>()
    val weeklyRankList: LiveData<List<RankListDto>> = _weeklyRankList

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
}
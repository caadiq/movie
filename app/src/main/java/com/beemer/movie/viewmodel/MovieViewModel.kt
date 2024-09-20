package com.beemer.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.movie.model.dto.DailyRankListDto
import com.beemer.movie.model.dto.WeeklyRankListDto
import com.beemer.movie.model.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _dailyRankList = MutableLiveData<List<DailyRankListDto>>()
    val dailyRankList: LiveData<List<DailyRankListDto>> = _dailyRankList

    private val _weeklyRankList = MutableLiveData<List<WeeklyRankListDto>>()
    val weeklyRankList: LiveData<List<WeeklyRankListDto>> = _weeklyRankList

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
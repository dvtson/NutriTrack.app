package com.example.nutritionapp.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritionapp.data.local.entity.DailyRecordEntity
import com.example.nutritionapp.data.local.entity.UserEntity
import com.example.nutritionapp.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: NutritionRepository
) : ViewModel() {

    val dailyRecords: StateFlow<List<DailyRecordEntity>> = repository.getAllDailyRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userProfile: StateFlow<UserEntity?> = repository.getUserProfile()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun deleteRecord(date: String) {
        viewModelScope.launch {
            repository.deleteHistoryForDate(date)
        }
    }
}

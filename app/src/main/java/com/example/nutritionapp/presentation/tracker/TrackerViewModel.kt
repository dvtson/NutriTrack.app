package com.example.nutritionapp.presentation.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutritionapp.data.local.entity.DailyRecordEntity
import com.example.nutritionapp.data.local.entity.UserEntity
import com.example.nutritionapp.data.local.entity.WeightRecordEntity
import com.example.nutritionapp.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val repository: NutritionRepository
) : ViewModel() {

    val dailyRecords: StateFlow<List<DailyRecordEntity>> = repository.getAllDailyRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weightRecords: StateFlow<List<WeightRecordEntity>> = repository.getWeightRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserEntity?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Hàm logWeight đã được loại bỏ để tập trung nhiệm vụ Tracking Cân Nặng cho Onboarding/Profile Save
    // Nhờ sự tập trung này, source code tránh được phân mảnh và lặp dữ liệu
}

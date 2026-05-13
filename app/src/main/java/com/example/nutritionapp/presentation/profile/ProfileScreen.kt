package com.example.nutritionapp.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutritionapp.presentation.MainViewModel
import com.example.nutritionapp.presentation.onboarding.OnboardingScreen

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()

    OnboardingScreen(
        existingUser = userProfile,
        onSaveProfile = { user ->
            viewModel.saveProfile(user)
            onNavigateBack() // Also navigate back after saving
        },
        onCancel = onNavigateBack
    )
}

package com.example.nutritionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.nutritionapp.presentation.dashboard.DashboardScreen
import com.example.nutritionapp.presentation.history.HistoryScreen
import com.example.nutritionapp.presentation.tracker.TrackerScreen
import com.example.nutritionapp.presentation.search.FoodSearchScreen
import com.example.nutritionapp.ui.theme.NutritionAppTheme
import androidx.compose.material3.NavigationBarItemDefaults
import com.example.nutritionapp.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutritionAppTheme {
                val navController = rememberNavController()
                val mainViewModel: com.example.nutritionapp.presentation.MainViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                val userProfile by mainViewModel.userProfile.collectAsState()
                
                Scaffold(
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        
                        val items = listOf(
                            com.example.nutritionapp.presentation.navigation.BottomNavItem.Home,
                            com.example.nutritionapp.presentation.navigation.BottomNavItem.Tracker,
                            com.example.nutritionapp.presentation.navigation.BottomNavItem.History,
                            com.example.nutritionapp.presentation.navigation.BottomNavItem.AddMeal
                        )
                        
                        // Show bottom bar only on top level routes AND when user has finished onboarding
                        val showBottomBar = currentRoute in items.map { it.route } && userProfile != null
                        
                        if (showBottomBar) {
                            NavigationBar {
                                items.forEach { item ->
                                    NavigationBarItem(
                                        icon = { Icon(item.icon, contentDescription = item.title) },
                                        label = { Text(item.title) },
                                        selected = currentRoute == item.route,
                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                            indicatorColor = Color.Transparent,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        ),
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController, 
                        startDestination = com.example.nutritionapp.presentation.navigation.BottomNavItem.Home.route,
                        modifier = Modifier.padding(paddingValues),
                        enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it }) + fadeIn(animationSpec = tween(300)) },
                        exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it }) + fadeOut(animationSpec = tween(300)) },
                        popEnterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(300)) },
                        popExitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { it }) + fadeOut(animationSpec = tween(300)) }
                    ) {
                        composable(com.example.nutritionapp.presentation.navigation.BottomNavItem.Home.route) {
                            DashboardScreen(
                                onNavigateToProfile = { navController.navigate("profile") }
                            )
                        }
                        composable(com.example.nutritionapp.presentation.navigation.BottomNavItem.Tracker.route) {
                            TrackerScreen()
                        }
                        composable(com.example.nutritionapp.presentation.navigation.BottomNavItem.History.route) {
                            HistoryScreen()
                        }
                        composable("profile") {
                            com.example.nutritionapp.presentation.profile.ProfileScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(com.example.nutritionapp.presentation.navigation.BottomNavItem.AddMeal.route) {
                            FoodSearchScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
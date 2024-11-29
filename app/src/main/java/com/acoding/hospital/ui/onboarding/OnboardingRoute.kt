package com.acoding.hospital.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.acoding.hospital.ui.login.navigateToLogin

private const val ROUTE = "onboarding"

fun NavGraphBuilder.onboardingScreen(navController: NavHostController) {
    composable(route = ROUTE) {
        OnboardingScreen(
            modifier = Modifier.fillMaxSize(),
            onGetStartedClick = {
                navController.navigateToLogin()
            }
        )
    }
}
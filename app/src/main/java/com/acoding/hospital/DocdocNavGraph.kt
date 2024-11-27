package com.acoding.hospital

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.acoding.hospital.ui.login.loginScreen
import com.acoding.hospital.ui.onboarding.onboardingScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val startDestination = "login"

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        onboardingScreen(navController)
        loginScreen(navController)
    }
}
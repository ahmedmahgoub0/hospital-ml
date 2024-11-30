package com.acoding.hospital

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.acoding.hospital.ui.SearchScreen
import com.acoding.hospital.ui.bio.BioScreen
import com.acoding.hospital.ui.home.HomeScreen
import com.acoding.hospital.ui.home.HomeViewModel
import com.acoding.hospital.ui.login.loginScreen
import com.acoding.hospital.ui.onboarding.onboardingScreen
import com.acoding.hospital.ui.profile_activity.ProfileScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val startDestination = "onboarding"
    val homeViewModel: HomeViewModel = koinViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        onboardingScreen(navController)
        loginScreen(navController)
        homeScreen(navController, homeViewModel)
        profileScreen(navController, homeViewModel)
        searchScreen(navController, homeViewModel)
        profileFromNotification(navController, homeViewModel)
    }
}

private const val HOME_ROUTE = "homeScreen"
fun NavGraphBuilder.homeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    composable(
        route = HOME_ROUTE,
    ) {
//        val viewModel = koinViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel.event

        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            state = state,
            onClickPatient = { it, index ->
                viewModel.clickPatient(it, index)
                navController.navigateToProfile()
            },
            onLanguageChanged = {
                viewModel.setLanguage(it)
            },
            setTabIndexType = {
                viewModel.setTabTypeIndex(it)
            },
            searchClicked = { navController.navigateToSearch() },
            searchClosed = { }
        )
    }
}

fun NavController.navigateToHome() {
    navigate(HOME_ROUTE) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

private const val PROFILE_NOTIFICATION = "patientProfile/{patientId}"

fun NavGraphBuilder.profileFromNotification(
    navController: NavController,
    viewModel: HomeViewModel
) {
    composable(
        route = "patientProfile/{patientId}",
        arguments = listOf(navArgument("patientId") { type = NavType.IntType })
    ) { backStackEntry ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val patientId = backStackEntry.arguments?.getInt("patientId") ?: 0

        if (patientId == 0) {

        } else {
            ProfileScreen(
                navController = navController, patientId = 3990, setPatient = {},
                state = state,
                filter = {
                    viewModel.filterByDate(it)
                },
                modifier = Modifier.fillMaxSize(),
                onBack = {
                    navController.navigateToHome()
                },
            )
        }
    }
}

fun NavController.navigateToProfileWithNotification(route: String) {
    navigate(route)
}

private const val Profile_ROUTE = "profileScreen"
fun NavGraphBuilder.profileScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    composable(
        route = Profile_ROUTE,
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle()

        BioScreen(
            modifier = Modifier.fillMaxSize(),
            state = state,
            filter = { date ->
                viewModel.filterByDate(date)
            },
            onBack = {
                /* TODO: */
                navController.navigateUp()
            }
        )
    }
}

fun NavController.navigateToProfile() {
    navigate(Profile_ROUTE) {
    }
}

private const val search_ROUTE = "searchScreen"
fun NavGraphBuilder.searchScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    composable(
        route = search_ROUTE,
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle()

        SearchScreen(
            modifier = Modifier.fillMaxSize(),
            list =
            state.patients,
            onBack = {
                navController.navigateUp()
            },
            onClickPatient = { it, index ->
                viewModel.clickPatient(it, index)
                navController.navigateToProfile()
            },
        )
    }
}

fun NavController.navigateToSearch() {
    navigate(search_ROUTE) {
    }
}

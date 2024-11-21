package com.acoding.hospital.ui.login

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.acoding.hospital.domain.util.ObserveAsEvents
import com.acoding.hospital.domain.util.toString
import org.koin.androidx.compose.koinViewModel

private val ROUTE = "login"

fun NavGraphBuilder.loginScreen(navController: NavHostController) {
    composable(
        route = ROUTE,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 }, // Start from the left
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 }, // Start from the left
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 }, // Exit to the right
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 }, // Exit to the right
                animationSpec = tween(300)
            )
        }
    ) {
        val viewModel = koinViewModel<LoginViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        val event = viewModel.event
        val context = LocalContext.current

        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            state = state,
            onUsernameChanged = viewModel::onUsernameChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
            onLogin = viewModel::login,
        )

        ObserveAsEvents(
            event = event
        ) {
            when (it) {
                is LoginEvent.ShowError -> {
                    Toast.makeText(
                        context,
                        it.message.toString(context = context),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

    }
}

fun NavController.navigateToLogin() {
    navigate(ROUTE) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}
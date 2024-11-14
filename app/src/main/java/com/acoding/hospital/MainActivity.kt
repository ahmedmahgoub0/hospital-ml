package com.acoding.hospital

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.acoding.hospital.domain.util.ObserveAsEvents
import com.acoding.hospital.ui.bio.BioScreen
import com.acoding.hospital.ui.home.HomeListEvent
import com.acoding.hospital.ui.home.HomeScreen
import com.acoding.hospital.ui.home.HomeViewModel
import com.acoding.hospital.ui.login.LoginEvent
import com.acoding.hospital.ui.login.LoginScreen
import com.acoding.hospital.ui.login.LoginViewModel
import com.acoding.hospital.ui.theme.HospitalTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewModel = koinViewModel<LoginViewModel>()
                    val state by viewModel.uiState.collectAsStateWithLifecycle()

                    ObserveAsEvents(event = viewModel.event) {
                        when (it) {
                            is LoginEvent.ShowError -> {
                                Toast.makeText(
                                    this,
                                    it.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        state = state,
                        onUsernameChanged = viewModel::onUsernameChanged,
                        onPasswordChanged = viewModel::onPasswordChanged,
                        onLogin = viewModel::login,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalAnimationApi::class)
@Composable
fun AdaptiveCoinListDetailPain(
    viewModel: HomeViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(event = viewModel.event) {
        when (it) {
            is HomeListEvent.Error -> {


            }
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        modifier = modifier,
        listPane = {
            HomeScreen(
                state = state,
                onClick = {
                    viewModel.clickPatient(it)
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail
                    )
                }
            )
        },
        detailPane = {

            BioScreen(
                state = state,
                filter = { start, end ->
                    viewModel.filterByDate(start, end)
                }
            )
        }
    )
}
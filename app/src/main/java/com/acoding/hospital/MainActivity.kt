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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.acoding.hospital.data.datastore.UserPreferences
import com.acoding.hospital.data.repo.HospitalRepo
import com.acoding.hospital.domain.util.ObserveAsEvents
import com.acoding.hospital.domain.util.toString
import com.acoding.hospital.helpers.updateConfiguration
import com.acoding.hospital.ui.bio.BioScreen
import com.acoding.hospital.ui.home.HomeListEvent
import com.acoding.hospital.ui.home.HomeScreen
import com.acoding.hospital.ui.home.HomeViewModel
import com.acoding.hospital.ui.theme.HospitalTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {

            val viewModel: MainViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            var local =
                if (state.userPreferences?.language?.locale == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

            updateConfiguration(
                context = this,
                language = state.userPreferences?.language?.locale ?: "en",
                rtl = state.userPreferences?.language?.locale == "ar"
            )

            HospitalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CompositionLocalProvider(LocalLayoutDirection provides local) {
                        val navController = rememberNavController()
                        MainNavGraph(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            navController = navController
                        )
                    }
                }
            }
        }

    }
}

@Immutable
data class MainState(
    val userPreferences: UserPreferences? = null,
)

class MainViewModel(
    private val repo: HospitalRepo
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.userDate.collect { userPreferences ->
                _state.update {
                    it.copy(
                        userPreferences = userPreferences
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalAnimationApi::class)
@Composable
fun AdaptiveCoinListDetailPain(
    hospitalId: Int = 0,
    viewModel: HomeViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(event = viewModel.event) {
        when (it) {
            is HomeListEvent.Error -> {
                Toast.makeText(
                    context,
                    it.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
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
                onClick = { it, index ->
                    viewModel.clickPatient(it, index)
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail
                    )
                },
                onLanguageChanged = {
                    viewModel.setLanguage(it)
                },
                setTabIndexType = {
                    viewModel.setTabTypeIndex(it)
                }
            )
        },
        detailPane = {
            BioScreen(
                state = state,
                filter = { start, end ->
                    viewModel.filterByDate(start, end)
                },
                onBack = {
                    navigator.navigateBack()
                }
            )
        }
    )
}
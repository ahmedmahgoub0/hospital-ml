package com.acoding.hospital.ui.profile_activity
//
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.unit.LayoutDirection
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavController
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.acoding.hospital.MainNavGraph
//import com.acoding.hospital.MainViewModel
//import com.acoding.hospital.helpers.updateConfiguration
//import com.acoding.hospital.ui.home.HomeViewModel
//import com.acoding.hospital.ui.theme.HospitalTheme
//import com.google.android.gms.tasks.Task
//import com.google.firebase.messaging.FirebaseMessaging
//import org.koin.androidx.compose.koinViewModel
//
//class ProfileActivity : ComponentActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//
//            HospitalTheme {
//                enableEdgeToEdge()
//
//                FirebaseMessaging.getInstance().subscribeToTopic("all-users")
//                    .addOnCompleteListener { task: Task<Void?> ->
//                        var msg = "Subscribed to topic!"
//                        if (!task.isSuccessful) {
//                            msg = "Subscription failed."
//                        }
//                        Log.d("FCM", msg)
//                    }
//
//                setContent {
//
//                    val viewModel: MainViewModel = koinViewModel()
//                    val state by viewModel.state.collectAsStateWithLifecycle()
//                    var local =
//                        if (state.userPreferences?.language?.locale == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
//
//                    val patientId = state.userPreferences?.patientId?.toInt()
//
//                    updateConfiguration(
//                        context = this,
//                        language = state.userPreferences?.language?.locale ?: "en",
//                        rtl = state.userPreferences?.language?.locale == "ar"
//                    )
//
//                    HospitalTheme {
//                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                            CompositionLocalProvider(LocalLayoutDirection provides local) {
//                                val navController = rememberNavController()
//
//                                Log.i("patientIdInMainActivity", "onCreate: $patientId")
//
//                                MainNavGraph(
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .padding(innerPadding),
//                                    navController = navController
//                                )
//
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ProfileNavGraph(
//    modifier: Modifier = Modifier,
//    navController: NavHostController,
//    patientId: Int,
//    viewModel: HomeViewModel
//) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//    val setPatient = viewModel::setPatient
//    val filter = viewModel::filterByDate
//
//    NavHost(
//        modifier = modifier,
//        navController = navController,
//        startDestination = "patientProfile/{patientId}"
//    ) {
//        composable(
//            route = "patientProfile/{patientId}",
//            arguments = listOf(navArgument("patientId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            ProfileScreen(
//                navController = navController,
//                modifier = modifier,
//                patientId = patientId,
//                setPatient = setPatient,
//                state = state,
//                filter = filter,
//                onBack = {
//
//                }
//            )
//        }
//    }
//}
//
//
//private const val PROFILE_NOTIFICATION = "patientProfile/{patientId}"
//
//fun NavGraphBuilder.profileFromNotification(
//    navController: NavController,
//    viewModel: HomeViewModel
//) {
//    composable(
//        route = "patientProfile/{patientId}",
//        arguments = listOf(navArgument("patientId") { type = NavType.IntType })
//    ) { backStackEntry ->
//        val state by viewModel.state.collectAsStateWithLifecycle()
//        val patientId = backStackEntry.arguments?.getInt("patientId") ?: 0
//
//        if (patientId == 0) {
//
//        } else {
//            ProfileScreen(
//                navController = navController, patientId = 3990, setPatient = {},
//                state = state,
//                filter = {
//                    viewModel.filterByDate(it)
//                },
//                modifier = Modifier.fillMaxSize(),
//                onBack = {
//
//                },
//            )
//        }
//    }
//}
//
//fun NavController.navigateToProfileWithNotification(route: String) {
//    navigate(route)
//}
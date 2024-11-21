package com.acoding.hospital.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.acoding.hospital.MainViewModel
import com.acoding.hospital.data.datastore.UserPreferencesData
import com.acoding.hospital.data.datastore.UserPreferencesSerializer
import com.acoding.hospital.data.networking.HttpClientFactory
import com.acoding.hospital.data.repo.HospitalRepo
import com.acoding.hospital.data.repo.HospitalRepoImpl
import com.acoding.hospital.ui.home.HomeViewModel
import com.acoding.hospital.ui.login.LoginViewModel
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.io.File

const val USER_PREFERENCES = "user_pref.pb"

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::HospitalRepoImpl) { bind<HospitalRepo>() }

    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MainViewModel)

    single<DataStore<UserPreferencesData>> {
        DataStoreFactory.create(
            serializer = get<UserPreferencesSerializer>(),
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ) {
            File(androidContext().filesDir, USER_PREFERENCES)
        }
    }

    single { UserPreferencesSerializer }
}
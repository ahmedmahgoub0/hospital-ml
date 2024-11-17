package com.acoding.hospital.di

import com.acoding.hospital.data.networking.HttpClientFactory
import com.acoding.hospital.data.repo.HospitalRepo
import com.acoding.hospital.data.repo.HospitalRepoImpl
import com.acoding.hospital.ui.home.HomeViewModel
import com.acoding.hospital.ui.login.LoginViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::HospitalRepoImpl) { bind<HospitalRepo>() }

    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
}
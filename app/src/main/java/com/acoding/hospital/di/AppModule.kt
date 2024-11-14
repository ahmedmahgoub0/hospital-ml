package com.acoding.hospital.di

import com.acoding.hospital.data.networking.HttpClientFactory
import com.acoding.hospital.ui.login.LoginViewModel
import com.acoding.hospital.ui.home.HomeViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }

    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
}
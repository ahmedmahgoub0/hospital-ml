package com.acoding.hospital

import android.app.Application
import com.acoding.hospital.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HospitalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@HospitalApplication)
            modules(appModule)
        }


    }
}
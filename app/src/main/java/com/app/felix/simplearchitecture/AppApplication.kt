package com.app.felix.simplearchitecture

import android.app.Application
import com.app.felix.simplearchitecture.data.network.netWorkModule
import com.app.felix.simplearchitecture.data.repository.repositoryModule
import com.app.felix.simplearchitecture.ui.main.mainViewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(
                listOf(
                    netWorkModule,
                    repositoryModule,
                    mainViewModule
                )
            )
        }
    }

}
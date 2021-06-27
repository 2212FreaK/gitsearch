package com.aurora.gitsearch

import android.app.Application
import com.aurora.gitsearch.di.gitSearchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GitSearchApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GitSearchApplication)
            modules(gitSearchModule)
        }
    }
}

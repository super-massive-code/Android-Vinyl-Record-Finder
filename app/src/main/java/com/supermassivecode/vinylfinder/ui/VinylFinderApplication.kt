package com.supermassivecode.vinylfinder.ui

import android.app.Application
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.WantedRecordsRepository
import com.supermassivecode.vinylfinder.ui.screens.RecordDetailViewModel
import com.supermassivecode.vinylfinder.ui.screens.SearchScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module

class VinylFinderApplication :  Application(), KoinComponent {

    private val koinModule = module {
        single { this }
        single { DiscogsRepository() }
        single { WantedRecordsRepository() }
        viewModel { SearchScreenViewModel(get()) }
        viewModel { RecordDetailViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VinylFinderApplication)
            modules(listOf(koinModule))
        }
    }
}
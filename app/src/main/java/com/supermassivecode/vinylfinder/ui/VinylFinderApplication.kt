package com.supermassivecode.vinylfinder.ui

import android.app.Application
import androidx.room.Room
import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.*
import com.supermassivecode.vinylfinder.data.local.room.FoundRecordDao
import com.supermassivecode.vinylfinder.data.local.room.VinylFinderRoomDatabase
import com.supermassivecode.vinylfinder.data.local.room.WantedRecordDao
import com.supermassivecode.vinylfinder.ui.screens.developeroptions.DeveloperOptionsViewModel
import com.supermassivecode.vinylfinder.ui.screens.search.RecordDetailViewModel
import com.supermassivecode.vinylfinder.ui.screens.search.SearchScreenViewModel
import com.supermassivecode.vinylfinder.ui.screens.wanted.FoundSellersViewModel
import com.supermassivecode.vinylfinder.ui.screens.wanted.WantedRecordsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class VinylFinderApplication : Application(), KoinComponent {

    private val ioDispatcher = "ioDispatcher"

    private val koinModule = module {
        single { this }
        single(named(ioDispatcher)) { Dispatchers.IO }
        single { DiscogsReleaseHTMLScraper(get()) }
        single { CurrencyUtils() }
        single { DiscogsRepository() }
        single { WantedFoundRecordsRepository(get(), get()) }
        single { provideDatabase(get()) }
        single { provideWantedRecordDao(get()) }
        single { provideFoundRecordDao(get()) }
        single { DiscogsWantedRecordWorker(get(), get(), get()) }
        viewModel { SearchScreenViewModel(get()) }
        viewModel { RecordDetailViewModel(get(), get()) }
        viewModel { WantedRecordsViewModel(get()) }
        viewModel { DeveloperOptionsViewModel(get()) }
        viewModel { FoundSellersViewModel(get())}
        worker { WantedRecordsWorkManager(get(), get()) }
    }

    private fun provideDatabase(application: Application): VinylFinderRoomDatabase {
        return Room.databaseBuilder(
            application,
            VinylFinderRoomDatabase::class.java, "vinyl-finder"
        ).build()
    }

    private fun provideWantedRecordDao(database: VinylFinderRoomDatabase): WantedRecordDao {
        return database.wantedRecordDao()
    }

    private fun provideFoundRecordDao(database: VinylFinderRoomDatabase): FoundRecordDao {
        return database.foundRecordDao()
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VinylFinderApplication)
            modules(listOf(koinModule))
        }
    }
}

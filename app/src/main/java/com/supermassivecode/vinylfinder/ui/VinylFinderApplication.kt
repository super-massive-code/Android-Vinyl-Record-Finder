package com.supermassivecode.vinylfinder.ui

import android.app.Application
import androidx.room.Room
import com.supermassivecode.vinylfinder.data.CurrencyUtils
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.DiscogsWantedRecordWorker
import com.supermassivecode.vinylfinder.data.local.WantedFoundRecordsRepository
import com.supermassivecode.vinylfinder.data.local.room.FoundRecordDao
import com.supermassivecode.vinylfinder.data.local.room.VinylFinderRoomDatabase
import com.supermassivecode.vinylfinder.data.local.room.WantedRecordDao
import com.supermassivecode.vinylfinder.ui.screens.RecordDetailViewModel
import com.supermassivecode.vinylfinder.ui.screens.SearchScreenViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class VinylFinderApplication :  Application(), KoinComponent {

    private val ioDispatcher = "ioDispatcher"

    private val koinModule = module {
        single { this }
        single (named(ioDispatcher)) { Dispatchers.IO }
        single { CurrencyUtils() }
        single { DiscogsRepository() }
        single { WantedFoundRecordsRepository(get(), get()) }
        single { provideDatabase(get()) }
        single { provideWantedRecordDao(get()) }
        single { provideFoundRecordDao(get())  }
        single { DiscogsWantedRecordWorker(get(), get(named(ioDispatcher)), get()) }
        viewModel { SearchScreenViewModel(get(), get()) }
        viewModel { RecordDetailViewModel(get(), get()) }
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

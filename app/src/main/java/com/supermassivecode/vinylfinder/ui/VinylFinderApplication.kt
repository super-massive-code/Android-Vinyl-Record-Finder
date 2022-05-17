package com.supermassivecode.vinylfinder.ui

import android.app.Application
import androidx.room.Room
import com.supermassivecode.vinylfinder.data.local.DiscogsRepository
import com.supermassivecode.vinylfinder.data.local.WantedRecordsRepository
import com.supermassivecode.vinylfinder.data.local.room.VinylFinderRoomDatabase
import com.supermassivecode.vinylfinder.data.local.room.WantedRecordDao
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
        single { WantedRecordsRepository(get()) }
        single { provideDatabase(get()) }
        single { provideWantedRecordDao(get()) }
        viewModel { SearchScreenViewModel(get()) }
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

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VinylFinderApplication)
            modules(listOf(koinModule))
        }
    }
}

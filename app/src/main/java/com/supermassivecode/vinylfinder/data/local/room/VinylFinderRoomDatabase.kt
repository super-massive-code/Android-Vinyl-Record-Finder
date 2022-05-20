package com.supermassivecode.vinylfinder.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [WantedRecord::class, FoundRecord::class], version = 1)
abstract class VinylFinderRoomDatabase: RoomDatabase() {
    abstract fun wantedRecordDao(): WantedRecordDao
    abstract fun foundRecordDao(): FoundRecordDao
}

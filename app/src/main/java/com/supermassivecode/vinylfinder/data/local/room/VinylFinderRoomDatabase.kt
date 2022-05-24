package com.supermassivecode.vinylfinder.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [WantedRecord::class, FoundRecord::class], version = 1)
@TypeConverters(ShopConvertor::class)
abstract class VinylFinderRoomDatabase: RoomDatabase() {
    abstract fun wantedRecordDao(): WantedRecordDao
    abstract fun foundRecordDao(): FoundRecordDao
}

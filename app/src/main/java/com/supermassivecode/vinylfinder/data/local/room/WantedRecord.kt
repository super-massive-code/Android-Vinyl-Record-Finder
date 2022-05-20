package com.supermassivecode.vinylfinder.data.local.room

import androidx.room.*
import java.util.*

@Entity
data class WantedRecord(
    @PrimaryKey val uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "discogs_remote_id") val discogsRemoteId: Int,
    @ColumnInfo(name = "record_title") val recordTitle: String,
    @ColumnInfo(name = "cat_no") val catNo: String,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "year") val year: String
)

@Dao
interface WantedRecordDao {

    @Query("SELECT * FROM WantedRecord")
    suspend fun getAll(): List<WantedRecord>

    @Insert
    suspend fun insert(record: WantedRecord)

    @Delete
    suspend fun delete(record: WantedRecord)
}

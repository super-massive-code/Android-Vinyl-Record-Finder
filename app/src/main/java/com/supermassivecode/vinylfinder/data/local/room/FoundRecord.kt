package com.supermassivecode.vinylfinder.data.local.room

import androidx.room.*
import java.util.*

@Entity
data class FoundRecord(
    @PrimaryKey val uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "wanted_record_id") val wantedRecordId: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "notes") val notes: String
)

@Dao
interface FoundRecordDao {

    @Query("SELECT * FROM FoundRecord WHERE wanted_record_id LIKE :parentId")
    suspend fun getAllForWantedRecord(parentId: String): List<FoundRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: FoundRecord)

    @Delete
    suspend fun delete(record: FoundRecord)
}
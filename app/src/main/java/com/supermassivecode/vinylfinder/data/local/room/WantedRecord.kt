package com.supermassivecode.vinylfinder.data.local.room

import androidx.room.*
import java.util.*

@Entity
data class WantedRecord(
    @PrimaryKey val uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "record_title") val recordTitle: String,
    @ColumnInfo(name = "cat_no") val catNo: String
)

/**
 * TODO: do we need more than record_title & cat_no?
 */

@Dao
interface WantedRecordDao {

    @Query("SELECT * FROM WantedRecord")
    fun getAll(): List<WantedRecord>

    @Insert
    fun insert(vararg record: WantedRecord)

    @Delete
    fun delete(record: WantedRecord)
}

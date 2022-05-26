package com.supermassivecode.vinylfinder.data.local.room

import androidx.room.*
import com.supermassivecode.vinylfinder.data.local.model.Shop
import java.util.*

@Entity
data class FoundRecord(
    @PrimaryKey val uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "time_stamp") val timeStamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "wanted_record_id") val wantedRecordId: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "seller") val seller: String,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "notes") val notes: String
)

class ShopConvertor {
    @TypeConverter
    fun fromSeller(name: String): Shop {
        return when (name) {
            Shop.DISCOGS.shopName -> Shop.DISCOGS
            Shop.EBAY.shopName -> Shop.EBAY
            else -> {
                throw IllegalStateException()
            }
        }
    }

    @TypeConverter
    fun toSeller(shop: Shop): String {
        return when (shop) {
            Shop.DISCOGS -> Shop.DISCOGS.shopName
            Shop.EBAY -> Shop.EBAY.shopName
        }
    }
}

@Dao
interface FoundRecordDao {

    @Query("SELECT * FROM FoundRecord WHERE wanted_record_id LIKE :parentId ORDER BY price ASC")
    suspend fun getAllForWantedRecord(parentId: String): List<FoundRecord>

    @Query("SELECT EXISTS(SELECT * FROM FoundRecord WHERE" +
            " url = :url AND" +
            " notes = :notes AND " +
            " price = :price)")
    suspend fun exists(url: String, notes: String, price: Float): Boolean

    @Insert
    suspend fun insert(record: FoundRecord)


    @Delete
    suspend fun delete(record: FoundRecord)
}

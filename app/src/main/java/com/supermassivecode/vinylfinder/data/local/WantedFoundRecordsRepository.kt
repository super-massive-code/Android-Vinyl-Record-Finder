package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.local.model.FoundRecordInfo
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import com.supermassivecode.vinylfinder.data.local.room.FoundRecord
import com.supermassivecode.vinylfinder.data.local.room.FoundRecordDao
import com.supermassivecode.vinylfinder.data.local.room.WantedRecord
import com.supermassivecode.vinylfinder.data.local.room.WantedRecordDao

class WantedFoundRecordsRepository(
    private val wantedRecordDao: WantedRecordDao,
    private val foundRecordDao: FoundRecordDao
) {
    suspend fun addWantedRecord(recordInfo: RecordInfo) {
        wantedRecordDao.insert(
            WantedRecord(
                discogsRemoteId = recordInfo.discogsRemoteId,
                recordTitle = recordInfo.title,
                catNo = recordInfo.catno,
                year = recordInfo.year,
                label = recordInfo.label
            )
        )

    }

    suspend fun getAllWantedRecords(): List<WantedRecord> {
        //TODO: convert these to RecordInfo / Obj to be consumed by UI?
        return wantedRecordDao.getAll()
    }

    suspend fun addFoundRecordIfNotExists(parentId: String, found: FoundRecordInfo) {
        if (!foundRecordDao.exists(found.url, found.seller, found.price)) {
            foundRecordDao.insert(
                FoundRecord(
                    wantedRecordId = parentId,
                    url = found.url,
                    notes = found.notes,
                    price = found.price,
                    seller = found.seller,
                    currency = found.currency
                )
            )
        }
    }

    suspend fun getFoundRecordsForParent(parentWantedRecordId: String): List<FoundRecord> {
        //TODO: convert these to RecordInfo / Obj to be consumed by UI?
        return foundRecordDao.getAllForWantedRecord(parentWantedRecordId)
    }
}

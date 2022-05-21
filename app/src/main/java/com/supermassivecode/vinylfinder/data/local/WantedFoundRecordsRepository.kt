package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import com.supermassivecode.vinylfinder.data.local.room.FoundRecord
import com.supermassivecode.vinylfinder.data.local.room.FoundRecordDao
import com.supermassivecode.vinylfinder.data.local.room.WantedRecord
import com.supermassivecode.vinylfinder.data.local.room.WantedRecordDao

class WantedFoundRecordsRepository(
    private val wantedRecordDao: WantedRecordDao,
    private val foundRecordDao: FoundRecordDao
) {
    suspend fun addWantedRecord(recordInfoDTO: RecordInfoDTO) {
        wantedRecordDao.insert(
            WantedRecord(
                discogsRemoteId = recordInfoDTO.discogsRemoteId,
                recordTitle = recordInfoDTO.title,
                catNo = recordInfoDTO.catno,
                year = recordInfoDTO.year,
                label = recordInfoDTO.label
            )
        )

    }

    suspend fun getAllWantedRecordsDTO(): List<RecordInfoDTO> {
        //TODO combine with group search to return associated found records?
        return wantedRecordDao.getAll().map {
            RecordInfoDTO(
                title = it.recordTitle,
                year = it.year,
                label = it.label,
                catno = it.catNo,
                discogsRemoteId = it.discogsRemoteId
            )
        }
    }

    suspend fun getAllWantedRecords(): List<WantedRecord> {
        return wantedRecordDao.getAll()
    }

    suspend fun addFoundRecordIfNotExists(parentId: String, found: FoundRecordDTO) {
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

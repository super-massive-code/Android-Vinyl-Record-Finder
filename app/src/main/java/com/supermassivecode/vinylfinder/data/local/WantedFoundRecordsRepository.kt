package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import com.supermassivecode.vinylfinder.data.local.model.WantedRecordDTO
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

    suspend fun getAllWantedRecords(): List<WantedRecord> {
        return wantedRecordDao.getAll()
    }

    suspend fun getAllWantedRecordsDTO(): List<WantedRecordDTO> {
        return wantedRecordDao.getAll().map { wanted ->
            val foundCount = foundRecordDao.getAllForWantedRecord(wanted.uid).size
            val info = RecordInfoDTO(
                title = wanted.recordTitle,
                year = wanted.year,
                label = wanted.label,
                catno = wanted.catNo,
                discogsRemoteId = wanted.discogsRemoteId
            )
            WantedRecordDTO(
                infoDTO = info,
                foundCount = foundCount,
                databaseUid = wanted.uid
            )
        }
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

    suspend fun getFoundRecordsForParent(parentWantedRecordId: String): List<FoundRecordDTO> {
        //TODO: Add image from seller type enum (associate image with name and use in workers)
        return foundRecordDao.getAllForWantedRecord(parentWantedRecordId).map {
            FoundRecordDTO(
                url = it.url,
                price = it.price,
                notes = it.notes,
                seller = it.seller,
                currency = it.currency
            )
        }
    }
}

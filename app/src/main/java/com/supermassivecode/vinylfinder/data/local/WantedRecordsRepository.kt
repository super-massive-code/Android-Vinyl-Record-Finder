package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import com.supermassivecode.vinylfinder.data.local.room.WantedRecord
import com.supermassivecode.vinylfinder.data.local.room.WantedRecordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WantedRecordsRepository(
    private val wantedRecordDao: WantedRecordDao
) {

    /**
     * TODO:
     * Create Room DB of wanted records
     * Use as source of store search workers
     */

    suspend fun addRecord(recordInfo: RecordInfo) {
        withContext(Dispatchers.IO) {
            wantedRecordDao.insert(WantedRecord(
                recordTitle = recordInfo.title,
                catNo = recordInfo.catno
            ))
        }
    }

    suspend fun getAll() {
        withContext(Dispatchers.IO) {
            wantedRecordDao.getAll()
        }
    }
}
package com.supermassivecode.vinylfinder.data.local.model

data class WantedRecordDTO(
    val infoDTO: RecordInfoDTO,
    val foundCount: Int,
    val databaseUid: String,
)

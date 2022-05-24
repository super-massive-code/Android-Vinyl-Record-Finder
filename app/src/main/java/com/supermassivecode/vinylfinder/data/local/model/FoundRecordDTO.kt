package com.supermassivecode.vinylfinder.data.local.model

data class FoundRecordDTO(
    val shop: Shop,
    val url: String,
    val price: Float,
    val notes: String = "",
    val currency: String
)

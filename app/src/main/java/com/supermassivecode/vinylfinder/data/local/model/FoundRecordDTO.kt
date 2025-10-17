package com.supermassivecode.vinylfinder.data.local.model

data class FoundRecordDTO(
    val shop: Shop,
    val url: String,
    val recordPrice: Float,
    val totalPriceIncShipping: Float,
    val notes: String = "",
    val currencyCode: String
)

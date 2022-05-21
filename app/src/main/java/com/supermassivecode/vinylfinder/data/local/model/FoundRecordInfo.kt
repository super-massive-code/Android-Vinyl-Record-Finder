package com.supermassivecode.vinylfinder.data.local.model

data class FoundRecordInfo(
    val url: String,
    val price: Float,
    val seller: String,
    val notes: String = "",
    val currency: String
)

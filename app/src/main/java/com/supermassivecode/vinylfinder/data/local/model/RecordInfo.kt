package com.supermassivecode.vinylfinder.data.local.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

@JsonClass(generateAdapter = true)
data class RecordInfo(
    val title: String,
    val imageUrl: String,
    val country: String,
    val year: String,
    val label: String,
    val catno: String,
    val discogsRemoteId: Int,
    val tracks: List<RecordTrack>? = null
) {
    fun toJson(): String {
        return adapter().toJson(this)
    }

    companion object {
        private fun adapter(): JsonAdapter<RecordInfo> {
            return Moshi.Builder().build().adapter(RecordInfo::class.java)
        }
        fun fromJson(json: String): RecordInfo? {
            return adapter().fromJson(json)
        }
    }
}

@JsonClass(generateAdapter = true)
data class RecordTrack(
    val title: String,
    val position: String
)

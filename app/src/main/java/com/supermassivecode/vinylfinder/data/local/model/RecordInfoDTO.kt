package com.supermassivecode.vinylfinder.data.local.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

@JsonClass(generateAdapter = true)
data class RecordInfoDTO(
    val title: String,
    val imageUrl: String? = null,
    val country: String? = null,
    val year: String,
    val label: String,
    val catno: String,
    val discogsRemoteId: Int,
    val tracks: List<RecordTrackDTO>? = null
) {
    fun toJson(): String {
        return adapter().toJson(this)
    }

    companion object {
        private fun adapter(): JsonAdapter<RecordInfoDTO> {
            return Moshi.Builder().build().adapter(RecordInfoDTO::class.java)
        }
        fun fromJson(json: String): RecordInfoDTO? {
            return adapter().fromJson(json)
        }
    }
}

@JsonClass(generateAdapter = true)
data class RecordTrackDTO(
    val title: String,
    val position: String
)

package com.supermassivecode.vinylfinder.data.local

import android.content.Context
import com.supermassivecode.vinylfinder.BuildConfig
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import com.supermassivecode.vinylfinder.data.remote.DiscogsService

class DiscogsRepository(
    appContext: Context
) {
    /**
     * TODO?:
     * Search Cache
     * 1. Keep x results in memory or disc (how long? Clear global cache or each obj?)
     * 2. If some search query comes in, then return response
     * 3. Cache release details also?
     */

    suspend fun search(query: String): List<RecordInfo>? {
        val response = DiscogsService.getService().search(
            token = BuildConfig.DISCOGS_API_TOKEN,
            query = query
        )
        if (response.isSuccessful) {
            return response.body()?.results?.map {
                RecordInfo(
                    title = it.title,
                    imageUrl = it.thumb,
                    country = it.country ?: "",
                    year = it.year ?: "",
                    label = it.label?.first() ?: "",
                    catno = it.catno ?:"",
                    discogsRemoteId = it.id
                )
            }
        }
        return null
    }

    suspend fun releaseDetail(record: RecordInfo): RecordInfo? {
        val response = DiscogsService.getService().releaseDetail(record.discogsRemoteId)
        if (response.isSuccessful) {
            response.body()?.let {
                var imageUrl = it.images.first().resource_url
                if (imageUrl.isNullOrBlank()) {
                    imageUrl = record.imageUrl
                }
                return RecordInfo(
                    title = record.title,
                    imageUrl = imageUrl,
                    discogsRemoteId = record.discogsRemoteId,
                    country = record.country,
                    year = record.year,
                    label = record.label,
                    catno = record.catno
                    //TODO: get tracks / videos etc
                )
            }
        }
        return null
    }
}

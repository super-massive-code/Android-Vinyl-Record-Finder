package com.supermassivecode.vinylfinder.data.local

import android.content.Context
import com.supermassivecode.vinylfinder.BuildConfig
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import com.supermassivecode.vinylfinder.data.local.model.RecordTrack
import com.supermassivecode.vinylfinder.data.remote.DiscogsService
import com.supermassivecode.vinylfinder.data.remote.model.Track

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

    //TODO: wrap these responses back to call site in data object <T> + response success/fail

    suspend fun releaseDetail(record: RecordInfo): RecordInfo? {
        val response = DiscogsService.getService().releaseDetail(
            token = BuildConfig.DISCOGS_API_TOKEN,
            releaseId= record.discogsRemoteId
        )
        if (response.isSuccessful) {
            response.body()?.let { detail ->
                return RecordInfo(
                    title = record.title,
                    imageUrl = detail.images.first().resource_url,
                    discogsRemoteId = record.discogsRemoteId,
                    country = record.country,
                    year = record.year,
                    label = record.label,
                    catno = record.catno,
                    tracks = detail.tracklist.map { track ->
                        RecordTrack(
                            title = track.title,
                            position = track.position
                        )
                    }
                )
            }
        }
        return null
    }
}

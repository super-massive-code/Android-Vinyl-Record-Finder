package com.supermassivecode.vinylfinder.data.local

import com.supermassivecode.vinylfinder.BuildConfig
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import com.supermassivecode.vinylfinder.data.local.model.RecordTrackDTO
import com.supermassivecode.vinylfinder.data.remote.discogs.DiscogsService
import com.supermassivecode.vinylfinder.data.remote.Response

class DiscogsRepository {
    /**
     * TODO?:
     * Search Cache
     * 1. Keep x results in memory or disc (how long? Clear global cache or each obj?)
     * 2. If some search query comes in, then return response
     * 3. Cache release details also?
     */

    suspend fun search(query: String): Response<List<RecordInfoDTO>?> {
        val response = DiscogsService.getService().search(
            token = BuildConfig.DISCOGS_API_TOKEN,
            query = query
        )
        if (response.isSuccessful) {
            return Response(response.body()?.results?.map {
                RecordInfoDTO(
                    title = it.title,
                    imageUrl = it.thumb,
                    country = it.country ?: "",
                    year = it.year ?: "",
                    label = it.label?.first() ?: "",
                    catno = it.catno ?: "",
                    discogsRemoteId = it.id
                )
            })
        }
        return Response(errorStringId = Response.parseServerErrorCode(response.code()))
    }

    suspend fun releaseDetail(record: RecordInfoDTO): Response<RecordInfoDTO> {
        val response = DiscogsService.getService().releaseDetail(
            token = BuildConfig.DISCOGS_API_TOKEN,
            releaseId = record.discogsRemoteId
        )
        if (response.isSuccessful) {
            response.body()?.let { detail ->
                return Response(
                    RecordInfoDTO(
                        title = record.title,
                        imageUrl = detail.images.first().resource_url,
                        discogsRemoteId = record.discogsRemoteId,
                        country = record.country,
                        year = record.year,
                        label = record.label,
                        catno = record.catno,
                        tracks = detail.tracklist.map { track ->
                            RecordTrackDTO(
                                title = track.title,
                                position = track.position
                            )
                        }
                    )
                )

            }
        }
        return Response(errorStringId = Response.parseServerErrorCode(response.code()))
    }
}

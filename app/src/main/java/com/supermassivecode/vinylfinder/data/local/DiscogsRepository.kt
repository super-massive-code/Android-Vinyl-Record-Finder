package com.supermassivecode.vinylfinder.data.local

import android.content.Context
import com.supermassivecode.vinylfinder.BuildConfig
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo
import com.supermassivecode.vinylfinder.data.remote.DiscogsService

class DiscogsRepository(
    appContext: Context
) {
    suspend fun search(query: String): List<RecordInfo>? {
        val response = DiscogsService.getService().search(
            token = BuildConfig.DISCOGS_API_TOKEN,
            query = query
        )
        if (response.isSuccessful) {
            return response.body()?.results?.map {
                RecordInfo(
                    title = it.title,
                    thumbUrl = it.thumb,
                    country = it.country ?: "",
                    year = it.year ?: "",
                    label = it.label?.first() ?: "",
                    catno = it.catno ?:"",
                    remoteUrl = it.resource_url
                )
            }
        }
        return null
    }
}

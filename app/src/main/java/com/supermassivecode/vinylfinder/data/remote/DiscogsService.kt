package com.supermassivecode.vinylfinder.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.supermassivecode.vinylfinder.data.remote.model.ReleaseResponse
import com.supermassivecode.vinylfinder.data.remote.model.SearchResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscogsService {

    @GET("/database/search")
    suspend fun search(
        @Query("token") token: String,
        @Query("q") query: String,
        @Query("type") type:String = "release"
        // TODO: maybe add UI selector / ENUM for this ^ One of release, artist, label
    ): Response<SearchResponse>

    @GET("/releases/{releaseId}")
    suspend fun releaseDetail(
        @Path("releaseId") releaseId: Int
    ): Response<ReleaseResponse>

    companion object {
        fun getService(): DiscogsService {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.discogs.com")
                .client(OkHttpClient())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
            return retrofit.create(DiscogsService::class.java)
        }
    }
}
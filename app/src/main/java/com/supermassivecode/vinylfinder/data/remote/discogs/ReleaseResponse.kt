package com.supermassivecode.vinylfinder.data.remote.discogs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReleaseResponse(
    val title: String,
    val id: Int,
    val images: List<Image>,
    val tracklist: List<Track>,
)

@JsonClass(generateAdapter = true)
data class Image(
    val height: Int,
    val resource_url: String,
    val width: Int
)

@JsonClass(generateAdapter = true)
data class Track(
    val duration: String,
    val position: String,
    val title: String,
    val artists: List<Artist>?
)

@JsonClass(generateAdapter = true)
data class Artist(
    val name: String,
)

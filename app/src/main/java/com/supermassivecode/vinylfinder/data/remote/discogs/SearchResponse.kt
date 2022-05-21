package com.supermassivecode.vinylfinder.data.remote.discogs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    val pagination: Pagination,
    val results: List<Result>,
)

@JsonClass(generateAdapter = true)
data class Pagination(
    val page: String
)

@JsonClass(generateAdapter = true)
data class Result(
    val id: Int,
    val title: String,
    val thumb: String,
    val country: String?,
    val year: String?,
    val label: List<String>?,
    val catno: String?
)

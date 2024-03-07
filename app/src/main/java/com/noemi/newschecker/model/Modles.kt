package com.noemi.newschecker.model

import com.squareup.moshi.Json

data class NewsResults(
    @Json(name = "results") val results: List<News>
)

data class News(
    @Json(name = "url") val url: String,
    @Json(name = "id") val id: Long,
    @Json(name = "published_date") val published_date: String,
    @Json(name = "byline") val byline: String,
    @Json(name = "title") val title: String,
    @Json(name = "abstract") val abstract: String
)
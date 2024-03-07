package com.noemi.newschecker.network

import com.noemi.newschecker.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewYorkTimesApi {

    @GET("1.json?")
    suspend fun getMostPopularNews(
        @Query("api-key") apiKey: String
    ): Response<NewsResults>
}
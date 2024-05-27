package com.noemi.newschecker.repository

import com.noemi.newschecker.model.*

interface NewYorkTimesRepository {

    suspend fun getMostPopularNews(key: String): NetworkResult<NewsResults>
}
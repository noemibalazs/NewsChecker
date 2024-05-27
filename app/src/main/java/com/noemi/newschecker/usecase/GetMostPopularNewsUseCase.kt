package com.noemi.newschecker.usecase

import com.noemi.newschecker.model.News

interface GetMostPopularNewsUseCase {

    suspend fun execute(key: String): Result<List<News>>
}
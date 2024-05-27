package com.noemi.newschecker.repository

import com.noemi.newschecker.helper.ApiResponseHelper
import com.noemi.newschecker.model.*
import com.noemi.newschecker.network.NewYorkTimesApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewYorkTimesRepositoryImpl @Inject constructor(
    private val apiResponseHelper: ApiResponseHelper,
    private val newYorkTimesApi: NewYorkTimesApi,
    private val dispatcher: CoroutineDispatcher
) : NewYorkTimesRepository {

    override suspend fun getMostPopularNews(key:String): NetworkResult<NewsResults> = withContext(dispatcher){
        apiResponseHelper.handleApiResponse { newYorkTimesApi.getMostPopularNews(key) }
    }
}
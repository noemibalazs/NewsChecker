package com.noemi.newschecker.helper

import com.noemi.newschecker.model.NetworkResult
import retrofit2.Response

interface ApiResponseHelper {

    suspend fun <T:Any> handleApiResponse(response: suspend () -> Response<T>): NetworkResult<T>
}
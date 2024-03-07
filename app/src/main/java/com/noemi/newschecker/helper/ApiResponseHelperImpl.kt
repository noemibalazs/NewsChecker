package com.noemi.newschecker.helper

import android.util.Log
import com.noemi.newschecker.model.NetworkResult
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class ApiResponseHelperImpl @Inject constructor() : ApiResponseHelper {

    override suspend fun <T : Any> handleApiResponse(response: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val apiResponse = response()
            when (apiResponse.isSuccessful && apiResponse.body() != null) {
                true -> NetworkResult.Success(apiResponse.body()!!)
                else -> NetworkResult.Error(
                    apiResponse.errorBody()?.string() ?: "Error while handleApiResponse(), error code: ${apiResponse.code()}, message: ${apiResponse.message()}"
                )
            }
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException while handleApiResponse(), error code: ${e.code()}, message: ${e.message}")
            NetworkResult.Error(e.message())
        } catch (e: Exception) {
            Log.e(TAG, "Exception while handleApiResponse(), message: ${e.message}")
            NetworkResult.Failure(e)
        }
    }

    companion object {
        private val TAG = ApiResponseHelperImpl::class.java.name
    }
}
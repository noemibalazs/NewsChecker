package com.noemi.newschecker.model

sealed class NetworkResult<out T : Any> {

    data class Success<T : Any>(val data: T) : NetworkResult<T>()
    data class Failure(val error: Throwable) : NetworkResult<Nothing>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}

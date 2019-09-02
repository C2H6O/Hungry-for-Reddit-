package net.doubov.api

sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Failure<T>(val exception: ApiResponseException) : ApiResponse<T>()
}
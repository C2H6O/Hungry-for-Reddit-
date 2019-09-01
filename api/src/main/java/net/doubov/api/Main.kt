package net.doubov.api

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.OkHttpClient

@UnstableDefault
fun main() = runBlocking {

    val okHttpClient = OkHttpClient.Builder().build()

    val redditApi = RedditApi(okHttpClient, Json(JsonConfiguration.Default.copy(strictMode = false)))

    val apiResponse = redditApi.fetchAnonymousAccessToken()

    when (apiResponse) {
        is ApiResponse.Success<AccessTokenResponse> -> println("AccessTokenResponse: $apiResponse.data")
        is ApiResponse.Failure -> println(apiResponse.exception)
    }

}
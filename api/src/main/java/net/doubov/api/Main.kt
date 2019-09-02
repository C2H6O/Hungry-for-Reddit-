package net.doubov.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.OkHttpClient

@UnstableDefault
fun main() = runBlocking {

    val okHttpClient = OkHttpClient.Builder().build()

    val redditApi = RedditApi(okHttpClient, Json(JsonConfiguration.Default.copy(strictMode = false)))

    launch {
        when (val apiResponse = withContext(Dispatchers.IO) { redditApi.fetchAnonymousAccessToken() }) {
            is ApiResponse.Success<AccessTokenResponse> -> println("AccessTokenResponse: $apiResponse")
            is ApiResponse.Failure -> println(apiResponse.exception)
        }
    }

    Unit
}
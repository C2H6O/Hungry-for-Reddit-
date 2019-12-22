package net.doubov.api

import kotlinx.serialization.json.Json
import net.doubov.api.RedditApi.Url.OAUTH
import net.doubov.api.models.NewsResponse
import net.doubov.core.di.AppScope
import net.doubov.core.network.ApiResponse
import net.doubov.core.network.ApiResponseException
import net.doubov.core.preferences.AppPreferences
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

@AppScope
class RedditApi @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val json: Json,
    private val appPreferences: AppPreferences
) {

    object Url {
        const val BASE = "https://www.reddit.com/api"
        const val OAUTH = "https://oauth.reddit.com"
    }

    fun fetchFrontPage(): ApiResponse<NewsResponse> {
        val request = Request
            .Builder()
            .url("$OAUTH/hot/.json")
            .addHeader("Authorization", "bearer ${appPreferences.anonymousAccessToken}")
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseString = response.body?.string()
                if (responseString != null) {
                    return ApiResponse.Success(
                        json.parse(NewsResponse.serializer(), responseString)
                    )
                }
            }
            return ApiResponse.Failure(ApiResponseException("Successful request with null body. Code: ${response.code} Request: ${response.request}"))
        } catch (exception: Exception) {
            return ApiResponse.Failure(
                ApiResponseException(exception.message, exception = exception)
            )
        }
    }
}
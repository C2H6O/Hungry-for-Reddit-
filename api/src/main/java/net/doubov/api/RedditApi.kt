package net.doubov.api

import kotlinx.serialization.json.Json
import net.doubov.api.RedditApi.Keys.DEVICE_ID
import net.doubov.api.RedditApi.Keys.GRANT_TYPE
import net.doubov.api.RedditApi.Url.ACCESS_TOKEN
import net.doubov.api.RedditApi.Url.OAUTH
import net.doubov.api.RedditApi.Values.CLIENT_ID
import net.doubov.api.RedditApi.Values.DO_NOT_TRACK
import net.doubov.api.RedditApi.Values.INSTALLED_CLIENT
import net.doubov.api.models.NewsResponse
import net.doubov.core.AppPreferences
import net.doubov.core.data.responses.AccessTokenResponse
import net.doubov.core.di.AppScope
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import javax.inject.Inject

@AppScope
class RedditApi @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val json: Json,
    private val appPreferences: AppPreferences
) {

    object Url {
        const val BASE = "https://www.reddit.com/api"
        const val ACCESS_TOKEN = "$BASE/v1/access_token"
        const val OAUTH = "https://oauth.reddit.com"
    }

    object Keys {
        const val GRANT_TYPE = "grant_type"
        const val DEVICE_ID = "device_id"
    }

    object Values {
        const val CLIENT_ID = "g-lxCgTE3iS4Sg"
        const val DO_NOT_TRACK = "DO_NOT_TRACK_THIS_DEVICE"
        const val INSTALLED_CLIENT = "$OAUTH/grants/installed_client"
    }

    fun fetchAnonymousAccessToken(): ApiResponse<AccessTokenResponse> {

        val body = MultipartBody
            .Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(DEVICE_ID, DO_NOT_TRACK)
            .addFormDataPart(GRANT_TYPE, INSTALLED_CLIENT)
            .build()

        val request = Request
            .Builder()
            .post(body)
            .url(ACCESS_TOKEN)
            .addHeader("Authorization", "Basic ${Base64.getEncoder().encodeToString("$CLIENT_ID:".toByteArray())}")
            .build()

        try {
            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                val responseString = response.body()?.string()
                println("Response string: $responseString")
                if (responseString != null) {
                    return ApiResponse.Success(
                        json.parse(
                            AccessTokenResponse.serializer(),
                            responseString
                        )
                    )
                }
            }

            return ApiResponse.Failure(ApiResponseException(response.message()))
        } catch (e: Exception) {
            return ApiResponse.Failure(ApiResponseException(e.message, exception = e))
        }
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
                val responseString = response.body()?.string()
                if (responseString != null) {
                    return ApiResponse.Success(
                        json.parse(
                            NewsResponse.serializer(),
                            responseString
                        )
                    )
                }
            }
            return ApiResponse.Failure(ApiResponseException("Successful request with null body. Request: ${response.request()}"))
        } catch (exception: Exception) {
            return ApiResponse.Failure(
                ApiResponseException(
                    exception.message,
                    exception = exception
                )
            )
        }
    }
}
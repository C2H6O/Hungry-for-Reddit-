package net.doubov.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.doubov.api.RedditApi.Keys.DEVICE_ID
import net.doubov.api.RedditApi.Keys.GRANT_TYPE
import net.doubov.api.RedditApi.Url.ACCESS_TOKEN
import net.doubov.api.RedditApi.Url.OAUTH
import net.doubov.api.RedditApi.Values.CLIENT_ID
import net.doubov.api.RedditApi.Values.DO_NOT_TRACK
import net.doubov.api.RedditApi.Values.INSTALLED_CLIENT
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Base64

class RedditApi(private val okHttpClient: OkHttpClient, private val json: Json) {

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

        val response = okHttpClient.newCall(request).execute()

        if (response.isSuccessful) {
            val responseString = response.body()?.string()
            println("Response string: $responseString")
            if (responseString != null) {
                return ApiResponse.Success(json.parse(AccessTokenResponse.serializer(), responseString))
            }
        }

        return ApiResponse.Failure(ApiResponseException(response.message()))
    }
}

class ApiResponseException(message: String? = null) : Exception(message)

sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Failure<T>(val exception: ApiResponseException) : ApiResponse<T>()
}

@Serializable
data class AccessTokenResponse(val access_token: String, val token_type: String)

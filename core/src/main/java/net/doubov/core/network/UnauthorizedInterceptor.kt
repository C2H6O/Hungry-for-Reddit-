package net.doubov.core.network

import kotlinx.serialization.json.Json
import net.doubov.core.AppPreferences
import net.doubov.core.data.responses.AccessTokenResponse
import net.doubov.core.network.UnauthorizedInterceptor.Keys.DEVICE_ID
import net.doubov.core.network.UnauthorizedInterceptor.Keys.GRANT_TYPE
import net.doubov.core.network.UnauthorizedInterceptor.Url.ACCESS_TOKEN
import net.doubov.core.network.UnauthorizedInterceptor.Url.OAUTH
import net.doubov.core.network.UnauthorizedInterceptor.Values.CLIENT_ID
import net.doubov.core.network.UnauthorizedInterceptor.Values.DO_NOT_TRACK
import net.doubov.core.network.UnauthorizedInterceptor.Values.INSTALLED_CLIENT
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class UnauthorizedInterceptor @Inject constructor(
    private val json: Json,
    private val appPreferences: AppPreferences
) : Interceptor {

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

    object Headers {
        const val AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        if (response.code == 401) {

            // Unauthorized
            val body = MultipartBody
                .Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(DEVICE_ID, DO_NOT_TRACK)
                .addFormDataPart(GRANT_TYPE, INSTALLED_CLIENT)
                .build()

            val tokenRequest = chain
                .request()
                .newBuilderWithoutHeaders()
                .post(body)
                .url(ACCESS_TOKEN)
                .addHeader(
                    Headers.AUTHORIZATION,
                    "Basic ${Base64.getEncoder().encodeToString("$CLIENT_ID:".toByteArray())}"
                )
                .build()

            response.close()

            val tokenResponse = chain.proceed(tokenRequest)

            if (tokenResponse.isSuccessful) {
                val tokenResponseString = tokenResponse.body?.string()
                tokenResponse.close()
                if (tokenResponseString != null) {
                    val accessTokenResponse = json.parse(
                        AccessTokenResponse.serializer(),
                        tokenResponseString
                    )
                    appPreferences.anonymousAccessToken = accessTokenResponse.access_token
                    // try the original request again.

                    val originalRequestWithNewToken = chain.request()
                        .newBuilder()
                        .removeHeader(Headers.AUTHORIZATION)
                        .addHeader(Headers.AUTHORIZATION, "bearer ${appPreferences.anonymousAccessToken}")
                        .build()

                    return chain.proceed(originalRequestWithNewToken)
                }
            }
        }

        return response
    }

    private fun Request.newBuilderWithoutHeaders(): Request.Builder {
        return newBuilder()
            .also { builder ->
                headers
                    .names()
                    .forEach { headerName -> builder.removeHeader(headerName) }
            }
    }
}
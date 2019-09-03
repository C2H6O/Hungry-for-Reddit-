package net.doubov.hungryforreddit.di.api

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import net.doubov.api.RedditApi
import net.doubov.hungryforreddit.di.AppScope
import okhttp3.OkHttpClient

@Module
object RedditApiModule {
    @Provides
    @AppScope
    fun redditApi(okHttpClient: OkHttpClient, json: Json): RedditApi {
        return RedditApi(okHttpClient, json)
    }
}
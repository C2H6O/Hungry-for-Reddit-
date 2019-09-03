package net.doubov.hungryforreddit.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.doubov.api.RedditApi
import net.doubov.hungryforreddit.App
import net.doubov.hungryforreddit.Preferences
import net.doubov.hungryforreddit.di.api.RedditApiModule
import okhttp3.OkHttpClient

@AppScope
@Component(modules = [AppModule::class, RedditApiModule::class])
interface AppComponent {

    fun redditApi(): RedditApi
    @AppContext
    fun appContext(): Context
    fun preferences(): Preferences

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance app: App,
            appModule: AppModule,
            redditApiModule: RedditApiModule
        ): AppComponent
    }
}

@Module
object AppModule {

    @AppContext
    @Provides
    @AppScope
    fun provideAppContext(app: App): Context {
        return app
    }

    @Provides
    @AppScope
    fun providePreferences(app: App): Preferences {
        return Preferences(app.getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE))
    }

    @UnstableDefault
    @Provides
    @AppScope
    fun provideKotlinSerializationJson(): Json {
        return Json(JsonConfiguration.Default.copy(strictMode = false))
    }

    @Provides
    @AppScope
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
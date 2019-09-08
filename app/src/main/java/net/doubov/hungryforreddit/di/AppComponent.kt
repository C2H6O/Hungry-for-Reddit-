package net.doubov.hungryforreddit.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.doubov.api.RedditApi
import net.doubov.core.AppPreferences
import net.doubov.core.di.AppContext
import net.doubov.core.di.AppScope
import net.doubov.hungryforreddit.App
import net.doubov.hungryforreddit.di.api.RedditApiModule
import okhttp3.OkHttpClient

@AppScope
@Component(
    modules = [
        AppModule::class,
        RedditApiModule::class,
        MainActivityModule::class,
        AndroidInjectionModule::class
    ]
)
interface AppComponent {

    fun redditApi(): RedditApi
    @AppContext
    fun appContext(): Context

    fun preferences(): AppPreferences

    fun inject(app: App)

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
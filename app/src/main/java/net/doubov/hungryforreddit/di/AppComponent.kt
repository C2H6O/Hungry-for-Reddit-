package net.doubov.hungryforreddit.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import net.doubov.api.RedditApi
import net.doubov.core.AppPreferences
import net.doubov.core.di.AppContext
import net.doubov.core.di.AppScope
import net.doubov.core.di.SerializationModule
import net.doubov.hungryforreddit.App
import net.doubov.hungryforreddit.di.android.MainActivityModule
import okhttp3.OkHttpClient

@AppScope
@Component(
    modules = [
        AppModule::class,
        MainActivityModule::class,
        SerializationModule::class,
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
            appModule: AppModule
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
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}
package net.doubov.hungryforreddit.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.api.RedditApi
import net.doubov.core.AppPreferences
import net.doubov.core.di.AppContext
import net.doubov.core.di.AppScope
import net.doubov.core.di.SerializationModule
import net.doubov.core.network.interceptors.UnauthorizedInterceptor
import net.doubov.hungryforreddit.App
import okhttp3.OkHttpClient

@AppScope
@Component(
    modules = [
        AppModule::class,
        SerializationModule::class
    ]
)
interface AppComponent : AppComponentInjections {

    fun inject(app: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance app: App
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
    fun okHttpClient(unauthorizedInterceptor: UnauthorizedInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            // add more interceptors here, before UnauthorizedInterceptor
            .addInterceptor(unauthorizedInterceptor)
            .build()
    }
}

interface AppComponentInjections {

    fun redditApi(): RedditApi
    @AppContext
    fun appContext(): Context

    fun preferences(): AppPreferences
}
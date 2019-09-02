package net.doubov.hungryforreddit.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.hungryforreddit.App
import net.doubov.hungryforreddit.Preferences

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {

    fun preferences(): Preferences

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

    @Provides
    @AppScope
    fun providePreferences(app: App): Preferences {
        return Preferences(app.getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE))
    }
}
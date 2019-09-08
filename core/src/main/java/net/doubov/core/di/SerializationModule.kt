package net.doubov.core.di

import dagger.Module
import dagger.Provides
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Module
object SerializationModule {
    @UnstableDefault
    @Provides
    @AppScope
    fun provideKotlinSerializationJson(): Json {
        return Json(JsonConfiguration.Default.copy(strictMode = false))
    }
}
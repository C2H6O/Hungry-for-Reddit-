package net.doubov.core.di

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.doubov.core.data.responses.thingModule

@Module
object SerializationModule {

    @Provides
    @AppScope
    fun provideKotlinSerializationJson(): Json {
        return Json(
            JsonConfiguration.Stable.copy(classDiscriminator = "kind", ignoreUnknownKeys = true),
            context = thingModule
        )
    }

}
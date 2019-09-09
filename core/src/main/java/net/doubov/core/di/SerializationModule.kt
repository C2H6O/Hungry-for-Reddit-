package net.doubov.core.di

import dagger.Module
import dagger.Provides
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.doubov.core.data.responses.thingModule

@Module
object SerializationModule {
    @UnstableDefault
    @Provides
    @AppScope
    fun provideKotlinSerializationJson(): Json {
        return Json(
            JsonConfiguration.Default.copy(strictMode = false, classDiscriminator = "kind"),
            context = thingModule
        )
    }
}
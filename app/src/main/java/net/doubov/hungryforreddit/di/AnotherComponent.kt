package net.doubov.hungryforreddit.di

import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.core.di.AnotherScope

@AnotherScope
@Component(
    modules = [
        AnotherModule::class,
        YetAnotherModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
interface AnotherComponent : AnotherComponentInjections {
    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): AnotherComponent
    }
}

@Module
object AnotherModule {
    @AnotherScope
    @Provides
    fun randomString(): String = "Wtf mate"
}

@Module
object YetAnotherModule {

    @AnotherScope
    @Provides
    fun randomInt(): Int = 42
}

interface AnotherComponentInjections : AppComponentInjections {
    fun randomString(): String
}
package net.doubov.hungryforreddit.di

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import net.doubov.core.di.AnotherScope
import net.doubov.hungryforreddit.SingleActivity

@AnotherScope
@Component(
    modules = [
        AnotherModule::class,
        YetAnotherModule::class,
        AndroidInjectionModule::class
    ],
    dependencies = [
        AppComponent::class
    ]
)
interface AnotherComponent : AnotherComponentInjections {

    fun inject(singleActivity: SingleActivity)

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent,
            anotherModule: AnotherModule,
            yetAnotherModule: YetAnotherModule
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
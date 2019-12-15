package net.doubov.hungryforreddit.di

import dagger.BindsInstance
import dagger.Component
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.SingleActivity

@ActivityScope
@Component(
    modules = [
        FragmentModule::class
    ],
    dependencies = [
        AnotherComponent::class
    ]
)
interface SingleActivityComponent : AnotherComponentInjections {

    fun inject(singleActivity: SingleActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance singleActivity: SingleActivity,
            anotherComponent: AnotherComponent
        ): SingleActivityComponent
    }
}
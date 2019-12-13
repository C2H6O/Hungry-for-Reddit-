package net.doubov.hungryforreddit.di

import dagger.BindsInstance
import dagger.Component
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.main.MainFragment

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

    fun mainFragment(): MainFragment

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance singleActivity: SingleActivity,
            singleActivityModule: FragmentModule = FragmentModule,
            anotherComponent: AnotherComponent
        ): SingleActivityComponent
    }
}
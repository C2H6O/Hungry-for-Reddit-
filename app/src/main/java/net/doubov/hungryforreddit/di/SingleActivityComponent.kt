package net.doubov.hungryforreddit.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.main.MainFragmentModule

@ActivityScope
@Component(
    modules = [
        FragmentModule::class,
        MainFragmentModule::class,
        AndroidInjectionModule::class
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
            singleActivityModule: FragmentModule = FragmentModule,
            anotherComponent: AnotherComponent
        ): SingleActivityComponent
    }
}
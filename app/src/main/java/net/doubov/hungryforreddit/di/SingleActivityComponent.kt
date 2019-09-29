package net.doubov.hungryforreddit.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.main.MainFragmentModule

@ActivityScope
@Component(
    modules = [
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
        fun create(anotherComponent: AnotherComponent): SingleActivityComponent
    }
}
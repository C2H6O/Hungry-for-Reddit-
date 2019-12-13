package net.doubov.main

import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Subcomponent
interface MainFragmentComponent : AndroidInjector<MainFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainFragment>
}

@Module(
    subcomponents = [MainFragmentComponent::class],
    includes = [MainFragmentModule.Bindings::class]
)
object MainFragmentModule {
    @Module
    interface Bindings {
        @Binds
        @IntoMap
        @ClassKey(MainFragment::class)
        fun bindAndroidInjectorFactory(factory: MainFragmentComponent.Factory): AndroidInjector.Factory<*>
    }
}
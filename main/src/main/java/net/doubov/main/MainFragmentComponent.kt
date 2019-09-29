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

@Module(subcomponents = [MainFragmentComponent::class])
abstract class MainFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(MainFragment::class)
    abstract fun bindAndroidInjectorFactory(factory: MainFragmentComponent.Factory): AndroidInjector.Factory<*>
}
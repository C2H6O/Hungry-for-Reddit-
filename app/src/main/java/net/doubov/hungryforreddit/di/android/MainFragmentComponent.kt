package net.doubov.hungryforreddit.di.android

import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Subcomponent
interface MainFragmentComponent : AndroidInjector<net.doubov.main.MainFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<net.doubov.main.MainFragment>
}

@Module(subcomponents = [MainFragmentComponent::class])
abstract class MainFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(net.doubov.main.MainFragment::class)
    abstract fun bindAndroidInjectorFactory(factory: MainFragmentComponent.Factory): AndroidInjector.Factory<*>
}
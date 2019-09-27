package net.doubov.hungryforreddit.di.android

import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Subcomponent(modules = [MainFragmentModule::class])
interface MainActivityComponent : AndroidInjector<net.doubov.main.MainActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<net.doubov.main.MainActivity>
}

@Module(subcomponents = [MainActivityComponent::class])
abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ClassKey(net.doubov.main.MainActivity::class)
    abstract fun bindAndroidInjectorFactory(factory: MainActivityComponent.Factory): AndroidInjector.Factory<*>
}
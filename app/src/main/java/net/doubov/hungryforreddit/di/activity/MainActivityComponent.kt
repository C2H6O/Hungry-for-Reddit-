package net.doubov.hungryforreddit.di.activity

import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import net.doubov.hungryforreddit.MainActivity
import net.doubov.hungryforreddit.di.fragment.MainFragmentModule

@Subcomponent(modules = [MainFragmentModule::class])
interface MainActivityComponent : AndroidInjector<MainActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainActivity>
}

@Module(subcomponents = [MainActivityComponent::class])
abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ClassKey(MainActivity::class)
    abstract fun bindAndroidInjectorFactory(factory: MainActivityComponent.Factory): AndroidInjector.Factory<*>
}
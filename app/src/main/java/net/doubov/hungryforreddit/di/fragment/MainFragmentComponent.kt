package net.doubov.hungryforreddit.di.fragment

import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import net.doubov.hungryforreddit.di.MainFragment

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
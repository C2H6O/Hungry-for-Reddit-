package net.doubov.hungryforreddit.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.doubov.core.InjectingFragmentFactory
import net.doubov.core.di.FragmentKey
import net.doubov.main.MainParentFragment

@Module(includes = [FragmentModule.Bindings::class])
object FragmentModule {

    @Module
    interface Bindings {
        @Binds
        fun provideFragmentFactory(injectingFragmentFactory: InjectingFragmentFactory): FragmentFactory

        @Binds
        @IntoMap
        @FragmentKey(MainParentFragment::class)
        fun provideMainParentFragment(mainParentFragment: MainParentFragment): Fragment
    }
}
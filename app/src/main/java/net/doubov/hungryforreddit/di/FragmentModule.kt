package net.doubov.hungryforreddit.di

import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import net.doubov.core.InjectingFragmentFactory

@Module(includes = [FragmentModule.Bindings::class])
object FragmentModule {

    @Module
    interface Bindings {
        @Binds
        fun provideFragmentFactory(injectingFragmentFactory: InjectingFragmentFactory): FragmentFactory
    }
}
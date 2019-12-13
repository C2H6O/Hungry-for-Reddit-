package net.doubov.main

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.Subcomponent
import net.doubov.core.di.FragmentScope

@FragmentScope
@Subcomponent(modules = [MainParentFragmentModule::class])
interface MainParentFragmentComponent {

    fun listFragmentFactory(): MainListFragment.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainParentFragmentComponent
    }

}

@AssistedModule
@Module(includes = [AssistedInject_MainParentFragmentModule::class])
object MainParentFragmentModule
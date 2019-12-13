package net.doubov.main

import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [MainParentFragmentModule::class])
interface MainParentFragmentComponent {

    fun createMainParentFragmentInstance(): MainParentFragment
    fun listFragmentComponentFactory(): MainListFragmentComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainParentFragmentComponent
    }
}

@Module
object MainParentFragmentModule
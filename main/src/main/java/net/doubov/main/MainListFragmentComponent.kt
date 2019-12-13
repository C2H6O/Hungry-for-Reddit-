package net.doubov.main

import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [MainListFragmentModule::class])
interface MainListFragmentComponent {

    fun createMainListFragmentInstance(): MainListFragment

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance listener: MainListFragment.Listener): MainListFragmentComponent
    }
}

@Module
object MainListFragmentModule
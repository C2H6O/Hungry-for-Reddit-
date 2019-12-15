package net.doubov.hungryforreddit.di.main.list

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.hungryforreddit.Router
import net.doubov.hungryforreddit.di.main.parent.MainParentBuilder
import net.doubov.main.MainListFragment
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainListScope

class MainListBuilder(
    private val dependency: MainParentBuilder.MainParentFragmentComponent
) {

    fun build(): MainListRouter {

        val fragment = MainListFragment()

        val component = DaggerMainListBuilder_MainListFragmentComponent
            .factory()
            .create(dependency, fragment)

        component.inject(fragment)

        return component.router
    }

    @MainListScope
    @Component(
        modules = [MainListFragmentModule::class],
        dependencies = [MainParentBuilder.MainParentFragmentComponent::class]
    )
    interface MainListFragmentComponent : Provisions {

        val router: MainListRouter

        fun inject(fragment: MainListFragment)

        @Component.Factory
        interface Factory {
            fun create(
                mainParentFragmentComponent: MainParentBuilder.MainParentFragmentComponent,
                @BindsInstance mainListFragment: MainListFragment
            ): MainListFragmentComponent
        }
    }

    interface Provisions : MainParentBuilder.Provisions

    @Module
    object MainListFragmentModule {
        @Provides
        @MainListScope
        fun provideRouter(fragment: MainListFragment, component: MainListFragmentComponent): MainListRouter {
            return MainListRouter(component, fragment)
        }
    }

}

class MainListRouter(
    component: MainListBuilder.MainListFragmentComponent,
    fragment: MainListFragment
) : Router<MainListBuilder.MainListFragmentComponent, MainListFragment>(component, fragment)
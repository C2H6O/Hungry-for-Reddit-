package net.doubov.hungryforreddit.di.main

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.main.DetailFragment
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDetailScope

class MainDetailBuilder(
    private val dependency: MainParentBuilder.MainParentFragmentComponent
) {

    fun buildParametrized(id: String, title: String): MainDetailRouter {
        val fragment = DetailFragment.newInstance(id, title)
        return buildRouter(fragment)
    }

    fun build(): MainDetailRouter {
        val fragment = DetailFragment()
        return buildRouter(fragment)
    }

    private fun buildRouter(fragment: DetailFragment): MainDetailRouter {
        val component = DaggerMainDetailBuilder_MainDetailFragmentComponent.factory()
            .create(dependency, fragment)

        component.inject(fragment)

        return component.router
    }

    @MainDetailScope
    @Component(
        modules = [MainDetailFragmentModule::class],
        dependencies = [MainParentBuilder.MainParentFragmentComponent::class]
    )
    interface MainDetailFragmentComponent :
        MainDetailFragmentInjections {

        val router: MainDetailRouter

        fun inject(fragment: DetailFragment)

        @Component.Factory
        interface Factory {
            fun create(
                mainParentFragmentComponent: MainParentBuilder.MainParentFragmentComponent,
                @BindsInstance detailFragment: DetailFragment
            ): MainDetailFragmentComponent
        }
    }

    interface MainDetailFragmentInjections : MainParentBuilder.MainParentFragmentInjections

    @Module
    object MainDetailFragmentModule {
        @Provides
        @MainDetailScope
        fun provideRouter(fragment: DetailFragment, component: MainDetailFragmentComponent): MainDetailRouter {
            return MainDetailRouter(component, fragment)
        }
    }

}

class MainDetailRouter(
    val component: MainDetailBuilder.MainDetailFragmentComponent,
    val fragment: DetailFragment
)
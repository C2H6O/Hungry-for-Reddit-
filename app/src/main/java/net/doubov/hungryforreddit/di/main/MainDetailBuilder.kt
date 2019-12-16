package net.doubov.hungryforreddit.di.main

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.hungryforreddit.MainDetailFragment
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDetailScope

class MainDetailBuilder(
    private val dependency: MainParentBuilder.MainParentFragmentComponent
) {

    fun buildParametrized(id: String, title: String): MainDetailRouter {
        val fragment = MainDetailFragment.newInstance(id, title)
        return buildRouter(fragment)
    }

    fun build(): MainDetailRouter {
        val fragment = MainDetailFragment()
        return buildRouter(fragment)
    }

    private fun buildRouter(fragment: MainDetailFragment): MainDetailRouter {
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

        fun inject(fragment: MainDetailFragment)

        @Component.Factory
        interface Factory {
            fun create(
                mainParentFragmentComponent: MainParentBuilder.MainParentFragmentComponent,
                @BindsInstance mainDetailFragment: MainDetailFragment
            ): MainDetailFragmentComponent
        }
    }

    interface MainDetailFragmentInjections : MainParentBuilder.MainParentFragmentInjections

    @Module
    object MainDetailFragmentModule {
        @Provides
        @MainDetailScope
        fun provideRouter(fragment: MainDetailFragment, component: MainDetailFragmentComponent): MainDetailRouter {
            return MainDetailRouter(component, fragment)
        }
    }

}

class MainDetailRouter(
    val component: MainDetailBuilder.MainDetailFragmentComponent,
    val fragment: MainDetailFragment
)
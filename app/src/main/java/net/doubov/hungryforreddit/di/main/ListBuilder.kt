package net.doubov.hungryforreddit.di.main

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.main.ListFragment
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainListScope

class ListBuilder(
    private val dependency: ParentBuilder.ParentFragmentComponent
) {

    fun build(): ListRouter {

        val fragment = ListFragment()

        val component = DaggerListBuilder_ListFragmentComponent.factory()
            .create(dependency, fragment)

        component.inject(fragment)

        return component.router
    }

    @MainListScope
    @Component(
        modules = [ListFragmentModule::class],
        dependencies = [ParentBuilder.ParentFragmentComponent::class]
    )
    interface ListFragmentComponent :
        ListFragmentInjections {

        val router: ListRouter

        fun inject(fragment: ListFragment)

        @Component.Factory
        interface Factory {
            fun create(
                parentFragmentComponent: ParentBuilder.ParentFragmentComponent,
                @BindsInstance listFragment: ListFragment
            ): ListFragmentComponent
        }
    }

    interface ListFragmentInjections : ParentBuilder.ParentFragmentInjections

    @Module
    object ListFragmentModule {

        @Provides
        @MainListScope
        fun provideRouter(fragment: ListFragment, component: ListFragmentComponent): ListRouter {
            return ListRouter(component, fragment)
        }
    }

}

class ListRouter(
    val component: ListBuilder.ListFragmentComponent,
    val fragment: ListFragment
)
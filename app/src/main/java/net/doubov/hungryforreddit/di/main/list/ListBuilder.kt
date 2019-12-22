package net.doubov.hungryforreddit.di.main.list

import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import net.doubov.hungryforreddit.di.main.DaggerListBuilder_ListFragmentComponent
import net.doubov.hungryforreddit.di.main.parent.ParentBuilder
import net.doubov.main.ListFragment
import net.doubov.main.routers.ListRouter
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

        return component.listRouter
    }

    @MainListScope
    @Component(
        modules = [ListFragmentModule::class],
        dependencies = [ParentBuilder.ParentFragmentComponent::class]
    )
    interface ListFragmentComponent : ListFragmentInjections {

        val listRouter: ListRouter

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

    @Module(includes = [ListFragmentModule.Bindings::class])
    object ListFragmentModule {
        @Module
        interface Bindings {
            @Binds
            fun bindListRouter(listRouterImpl: ListRouterImpl): ListRouter
        }
    }

}
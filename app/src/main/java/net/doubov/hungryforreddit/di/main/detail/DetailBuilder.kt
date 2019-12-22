package net.doubov.hungryforreddit.di.main.detail

import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import net.doubov.hungryforreddit.di.main.parent.ParentBuilder
import net.doubov.main.DetailFragment
import net.doubov.main.routers.DetailRouter
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDetailScope

class DetailBuilder(
    private val dependency: ParentBuilder.ParentFragmentComponent
) {

    fun buildParametrized(id: String, title: String): DetailRouter {
        val fragment = DetailFragment.newInstance(id, title)
        return buildRouter(fragment)
    }

    fun build(): DetailRouter {
        val fragment = DetailFragment()
        return buildRouter(fragment)
    }

    private fun buildRouter(fragment: DetailFragment): DetailRouter {
        val component = DaggerDetailBuilder_DetailFragmentComponent.factory()
            .create(dependency, fragment)

        component.inject(fragment)

        return component.detailRouter()
    }

    @MainDetailScope
    @Component(
        modules = [DetailFragmentModule::class],
        dependencies = [ParentBuilder.ParentFragmentComponent::class]
    )
    interface DetailFragmentComponent : DetailFragmentInjections {

        fun inject(fragment: DetailFragment)

        @Component.Factory
        interface Factory {
            fun create(
                parentFragmentComponent: ParentBuilder.ParentFragmentComponent,
                @BindsInstance detailFragment: DetailFragment
            ): DetailFragmentComponent
        }
    }

    interface DetailFragmentInjections : ParentBuilder.ParentFragmentInjections {
        fun detailRouter(): DetailRouter
    }

    @Module(includes = [DetailFragmentModule.Bindings::class])
    object DetailFragmentModule {

        @Module
        interface Bindings {
            @Binds
            fun bindDetailRouter(detailRouterImpl: DetailRouterImpl): DetailRouter
        }
    }

}
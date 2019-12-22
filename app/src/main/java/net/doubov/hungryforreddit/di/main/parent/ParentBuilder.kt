package net.doubov.hungryforreddit.di.main.parent

import androidx.fragment.app.FragmentFactory
import dagger.*
import net.doubov.hungryforreddit.di.SingleActivityBuilder
import net.doubov.hungryforreddit.di.main.detail.DetailBuilder
import net.doubov.hungryforreddit.di.main.list.ListBuilder
import net.doubov.main.ListFragment
import net.doubov.main.ParentFragment
import net.doubov.main.routers.ParentRouter
import java.util.*
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainParentScope

class ParentBuilder(
    private val dependency: SingleActivityBuilder.SingleActivityComponent
) {

    fun build(): ParentRouter {

        val fragment = ParentFragment()

        val component: ParentFragmentComponent = DaggerParentBuilder_ParentFragmentComponent.factory()
            .create(dependency, fragment)

        component.inject(fragment)

        return component.parentRouter
    }

    @MainParentScope
    @Component(
        modules = [ParentFragmentModule::class],
        dependencies = [SingleActivityBuilder.SingleActivityComponent::class]
    )
    interface ParentFragmentComponent : ParentFragmentInjections {

        fun inject(fragment: ParentFragment)

        @Component.Factory
        interface Factory {
            fun create(
                singleActivityComponent: SingleActivityBuilder.SingleActivityComponent,
                @BindsInstance fragment: ParentFragment
            ): ParentFragmentComponent
        }
    }

    interface ParentFragmentInjections : SingleActivityBuilder.SingleActivityInjections {
        fun provideListChannel(): ListFragment.ListChannel
        val parentRouter: ParentRouter
    }

    @Module(includes = [ParentFragmentModule.Bindings::class])
    object ParentFragmentModule {

        @Module
        interface Bindings {
            @Binds
            fun provideParentRouter(parentRouter: ParentRouterImpl): ParentRouter

            @BindsOptionalOf
            fun provideOptionalFragmentFactory(): Optional<FragmentFactory>
        }

        @Provides
        @MainParentScope
        fun provideFragmentFactory(parentFragmentFactory: ParentFragmentFactory): FragmentFactory {
            return parentFragmentFactory
        }

        @Provides
        @MainParentScope
        fun provideListEvents(): ListFragment.ListChannel {
            return ListFragment.ListChannel()
        }

        @Provides
        @MainParentScope
        fun provideListBuilder(component: ParentFragmentComponent): ListBuilder {
            return ListBuilder(component)
        }

        @Provides
        @MainParentScope
        fun provideDetailBuilder(component: ParentFragmentComponent): DetailBuilder {
            return DetailBuilder(component)
        }
    }

}
package net.doubov.hungryforreddit.di

import androidx.fragment.app.FragmentFactory
import dagger.*
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.activity.SingleActivity
import net.doubov.hungryforreddit.activity.SingleActivityFragmentFactory
import net.doubov.hungryforreddit.activity.SingleActivityRouter
import net.doubov.hungryforreddit.activity.SingleActivityRouterImpl
import net.doubov.hungryforreddit.di.main.parent.ParentBuilder

class SingleActivityBuilder(
    private val singleActivity: SingleActivity,
    private val anotherComponent: AnotherComponent
) {

    fun build(): SingleActivityRouter {

        val component: SingleActivityComponent = DaggerSingleActivityBuilder_SingleActivityComponent
            .factory()
            .create(singleActivity, anotherComponent)

        component.inject(singleActivity)

        return component.singleActivityRouter
    }

    @ActivityScope
    @Component(
        modules = [SingleActivityModule::class],
        dependencies = [
            AnotherComponent::class
        ]
    )
    interface SingleActivityComponent : SingleActivityInjections {

        fun inject(singleActivity: SingleActivity)

        @Component.Factory
        interface Factory {
            fun create(
                @BindsInstance singleActivity: SingleActivity,
                anotherComponent: AnotherComponent
            ): SingleActivityComponent
        }
    }

    @Module(includes = [SingleActivityModule.Bindings::class])
    object SingleActivityModule {

        @Module
        interface Bindings {
            @Binds
            fun provideFragmentFactory(singleActivityFragmentFactory: SingleActivityFragmentFactory): FragmentFactory

            @Binds
            fun provideRootRouter(rootRouterImpl: SingleActivityRouterImpl): SingleActivityRouter
        }

        @Provides
        @ActivityScope
        fun provideParentBuilder(component: SingleActivityComponent): ParentBuilder {
            return ParentBuilder(component)
        }
    }

    interface SingleActivityInjections : AnotherComponentInjections {
        fun activity(): SingleActivity
        val singleActivityRouter: SingleActivityRouter
    }

}
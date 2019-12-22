package net.doubov.hungryforreddit.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.*
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.hungryforreddit.di.main.ParentBuilder
import net.doubov.main.ParentFragment
import javax.inject.Inject

class SingleActivityBuilder(
    private val singleActivity: SingleActivity,
    private val anotherComponent: AnotherComponent
) {

    fun build(): RootRouter {

        val component: SingleActivityComponent = DaggerSingleActivityBuilder_SingleActivityComponent
            .factory()
            .create(singleActivity, anotherComponent)

        component.inject(singleActivity)

        return component.rootRouter
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
            fun provideFragmentFactory(rootFragmentFactory: RootFragmentFactory): FragmentFactory

            @Binds
            fun provideRootRouter(rootRouterImpl: RootRouterImpl): RootRouter
        }

        @Provides
        @ActivityScope
        fun provideParentBuilder(component: SingleActivityComponent): ParentBuilder {
            return ParentBuilder(component)
        }
    }

    interface SingleActivityInjections : AnotherComponentInjections {
        fun activity(): SingleActivity
        val rootRouter: RootRouter
    }

}

@ActivityScope
class RootFragmentFactory @Inject constructor(
    private val parentBuilder: ParentBuilder
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            ParentFragment::class.java -> parentBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}

interface RootRouter {
    fun goToMainParentFragment()
}

@ActivityScope
class RootRouterImpl @Inject constructor(
    private val activity: SingleActivity,
    private val parentBuilder: ParentBuilder
) : RootRouter {

    override fun goToMainParentFragment() {
        activity
            .supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fragmentContainer,
                parentBuilder.build().fragment
            )
            .commit()
    }
}
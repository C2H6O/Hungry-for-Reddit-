package net.doubov.hungryforreddit.di.main.parent

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.Router
import net.doubov.hungryforreddit.di.main.RootBuilder
import net.doubov.hungryforreddit.di.main.list.MainListBuilder
import net.doubov.main.MainListFragment
import net.doubov.main.MainParentFragment
import javax.inject.Inject
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainParentScope

@MainParentScope
class MainParentBuilder(
    private val dependency: RootBuilder.SingleActivityComponent
) {

    fun build(): MainParentRouter {

        val fragment = MainParentFragment()

        val component: MainParentFragmentComponent = DaggerMainParentBuilder_MainParentFragmentComponent
            .factory()
            .create(dependency, fragment)

        component.inject(fragment)

        return component.parentRouter
    }

    @MainParentScope
    @Component(
        modules = [MainParentFragmentModule::class],
        dependencies = [RootBuilder.SingleActivityComponent::class]
    )
    interface MainParentFragmentComponent : Provisions {

        fun inject(fragment: MainParentFragment)

        val parentRouter: MainParentRouter

        @Component.Factory
        interface Factory {
            fun create(
                singleActivityComponent: RootBuilder.SingleActivityComponent,
                @BindsInstance fragment: MainParentFragment
            ): MainParentFragmentComponent
        }
    }

    interface Provisions : RootBuilder.Provisions {
        fun provideMainListChannel(): MainListFragment.MainListChannel
    }

    @Module
    object MainParentFragmentModule {

        @Provides
        @MainParentScope
        fun provideMainListEvents(): MainListFragment.MainListChannel {
            return MainListFragment.MainListChannel()
        }

        @Provides
        @MainParentScope
        fun provideMainListBuilder(component: MainParentFragmentComponent): MainListBuilder {
            return MainListBuilder(component)
        }

        @Provides
        @MainParentScope
        fun provideRouter(
            fragment: MainParentFragment,
            component: MainParentFragmentComponent,
            mainListBuilder: MainListBuilder
        ): MainParentRouter {
            return MainParentRouter(component, fragment, mainListBuilder)
        }
    }

}

class MainParentFragmentFactory @Inject constructor(
    private val mainListBuilder: MainListBuilder
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            MainListFragment::class.java -> mainListBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}

class MainParentRouter(
    component: MainParentBuilder.MainParentFragmentComponent,
    fragment: MainParentFragment,
    private val mainListBuilder: MainListBuilder
) : Router<MainParentBuilder.MainParentFragmentComponent, MainParentFragment>(component, fragment) {

    fun goToListFragment() {
        val router = mainListBuilder.build()
        fragment.childFragmentManager
            .beginTransaction()
            .add(R.id.mainParentFragmentContainer, router.fragment)
            .commit()
        addChild(router)
    }
}
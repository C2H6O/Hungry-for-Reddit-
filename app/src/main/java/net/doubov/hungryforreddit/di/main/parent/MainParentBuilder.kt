package net.doubov.hungryforreddit.di.main.parent

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.api.models.NewsDataResponse
import net.doubov.hungryforreddit.MainDetailFragment
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.di.RootBuilder
import net.doubov.hungryforreddit.di.main.detail.MainDetailBuilder
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
    interface MainParentFragmentComponent : MainParentFragmentInjections {

        fun inject(fragment: MainParentFragment)

        @Component.Factory
        interface Factory {
            fun create(
                singleActivityComponent: RootBuilder.SingleActivityComponent,
                @BindsInstance fragment: MainParentFragment
            ): MainParentFragmentComponent
        }
    }

    interface MainParentFragmentInjections : RootBuilder.SingleActivityInjections {
        fun provideMainListChannel(): MainListFragment.MainListChannel
        val parentRouter: MainParentRouter
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
        fun provideMainDetailBuilder(component: MainParentFragmentComponent): MainDetailBuilder {
            return MainDetailBuilder(component)
        }

        @Provides
        @MainParentScope
        fun provideRouter(
            fragment: MainParentFragment,
            component: MainParentFragmentComponent,
            mainListBuilder: MainListBuilder,
            mainDetailBuilder: MainDetailBuilder
        ): MainParentRouter {
            return MainParentRouter(component, fragment, mainListBuilder, mainDetailBuilder)
        }
    }

}

class MainParentFragmentFactory @Inject constructor(
    private val mainListBuilder: MainListBuilder,
    private val mainDetailBuilder: MainDetailBuilder
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            MainListFragment::class.java -> mainListBuilder.build().fragment
            MainDetailFragment::class.java -> mainDetailBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}

class MainParentRouter(
    val component: MainParentBuilder.MainParentFragmentComponent,
    val fragment: MainParentFragment,
    private val mainListBuilder: MainListBuilder,
    private val mainDetailBuilder: MainDetailBuilder
) {

    fun goToListFragment() {
        fragment.childFragmentManager
            .beginTransaction()
            .add(R.id.mainParentFragmentContainer, mainListBuilder.build().fragment)
            .commit()
    }

    fun goToDetailFragment(newsDataResponse: NewsDataResponse) {
        fragment.childFragmentManager
            .beginTransaction()
            .replace(
                R.id.mainParentFragmentContainer,
                mainDetailBuilder.buildParametrized(newsDataResponse.id, newsDataResponse.title).fragment
            )
            .addToBackStack(null)
            .commit()
    }

}
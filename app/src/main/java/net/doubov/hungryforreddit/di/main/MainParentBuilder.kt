package net.doubov.hungryforreddit.di.main

import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import net.doubov.api.models.NewsDataResponse
import net.doubov.hungryforreddit.MainDetailFragment
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.hungryforreddit.di.RootBuilder
import net.doubov.main.MainListFragment
import net.doubov.main.MainParentFragment
import java.util.*
import javax.inject.Inject
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainParentScope

class MainParentBuilder(
    private val dependency: RootBuilder.SingleActivityComponent
) {

    fun build(): MainParentRouter {

        val fragment = MainParentFragment()

        val component: MainParentFragmentComponent = DaggerMainParentBuilder_MainParentFragmentComponent.factory()
            .create(dependency, fragment)

        component.inject(fragment)

        return component.parentRouter
    }

    @MainParentScope
    @Component(
        modules = [MainParentFragmentModule::class],
        dependencies = [RootBuilder.SingleActivityComponent::class]
    )
    interface MainParentFragmentComponent :
        MainParentFragmentInjections {

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
        fun provideOptionalFragmentFactory(
            mainParentFragmentFactory: MainParentFragmentFactory
        ): Optional<FragmentFactory> {
            return Optional.of(mainParentFragmentFactory)
        }


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
            activity: SingleActivity,
            fragment: MainParentFragment,
            component: MainParentFragmentComponent,
            mainListBuilder: MainListBuilder,
            mainDetailBuilder: MainDetailBuilder
        ): MainParentRouter {
            return MainParentRouter(
                activity,
                component,
                fragment,
                mainListBuilder,
                mainDetailBuilder
            )
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
    activity: AppCompatActivity,
    val component: MainParentBuilder.MainParentFragmentComponent,
    val fragment: MainParentFragment,
    private val mainListBuilder: MainListBuilder,
    private val mainDetailBuilder: MainDetailBuilder
) {

    init {
        activity
            .onBackPressedDispatcher
            .addCallback(fragment) {
                if (fragment.childFragmentManager.backStackEntryCount > 0) {
                    fragment.childFragmentManager.popBackStack()
                }
                isEnabled = fragment.childFragmentManager.backStackEntryCount > 1
            }
    }

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
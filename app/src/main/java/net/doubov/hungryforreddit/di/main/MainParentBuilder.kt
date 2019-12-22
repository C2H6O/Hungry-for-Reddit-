package net.doubov.hungryforreddit.di.main

import android.content.Context
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import dagger.*
import net.doubov.api.models.NewsDataResponse
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.hungryforreddit.di.RootBuilder
import net.doubov.main.DetailFragment
import net.doubov.main.ListFragment
import net.doubov.main.ParentFragment
import net.doubov.main.ParentRouter
import java.util.*
import javax.inject.Inject
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainParentScope

class MainParentBuilder(
    private val dependency: RootBuilder.SingleActivityComponent
) {

    fun build(): MainParentRouterImpl {

        val fragment = ParentFragment()

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

        fun inject(fragment: ParentFragment)

        @Component.Factory
        interface Factory {
            fun create(
                singleActivityComponent: RootBuilder.SingleActivityComponent,
                @BindsInstance fragment: ParentFragment
            ): MainParentFragmentComponent
        }
    }

    interface MainParentFragmentInjections : RootBuilder.SingleActivityInjections {
        fun provideMainListChannel(): ListFragment.MainListChannel
        val parentRouter: MainParentRouterImpl
    }

    @Module(includes = [MainParentFragmentModule.Bindings::class])
    object MainParentFragmentModule {

        @Module
        interface Bindings {
            @Binds
            fun provideParentRouter(mainParentRouter: MainParentRouterImpl): ParentRouter
        }

        @Provides
        @MainParentScope
        fun provideOptionalFragmentFactory(
            mainParentFragmentFactory: MainParentFragmentFactory
        ): Optional<FragmentFactory> {
            return Optional.of(mainParentFragmentFactory)
        }


        @Provides
        @MainParentScope
        fun provideMainListEvents(): ListFragment.MainListChannel {
            return ListFragment.MainListChannel()
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
            fragment: ParentFragment,
            fragmentFactory: MainParentFragmentFactory,
            component: MainParentFragmentComponent,
            mainListBuilder: MainListBuilder,
            mainDetailBuilder: MainDetailBuilder
        ): MainParentRouterImpl {
            return MainParentRouterImpl(
                activity,
                component,
                fragment,
                fragmentFactory,
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
            ListFragment::class.java -> mainListBuilder.build().fragment
            DetailFragment::class.java -> mainDetailBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}

class MainParentRouterImpl(
    val activity: SingleActivity,
    val component: MainParentBuilder.MainParentFragmentComponent,
    val fragment: ParentFragment,
    val fragmentFactory: MainParentFragmentFactory,
    private val mainListBuilder: MainListBuilder,
    private val mainDetailBuilder: MainDetailBuilder
) : ParentRouter {

    private val callback = activity.onBackPressedDispatcher
        .addCallback(fragment) {
            if (fragment.childFragmentManager.backStackEntryCount > 0) {
                fragment.childFragmentManager.popBackStack()
            }
        }
        .also { it.isEnabled = false }

    init {
        activity
            .supportFragmentManager
            .registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                        if (f == fragment) {
                            fragment.childFragmentManager.fragmentFactory = fragmentFactory
                            fragment.childFragmentManager.registerFragmentLifecycleCallbacks(object :
                                FragmentManager.FragmentLifecycleCallbacks() {
                                override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                                    super.onFragmentAttached(fm, f, context)
                                    when (f::class) {
                                        DetailFragment::class -> callback.isEnabled = true
                                    }
                                }

                                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                                    super.onFragmentDetached(fm, f)
                                    when (f::class) {
                                        DetailFragment::class -> callback.isEnabled = false
                                    }
                                }
                            }, true)
                        }
                    }
                },
                true
            )
    }

    override fun goToListFragment() {
        fragment.childFragmentManager
            .beginTransaction()
            .add(R.id.mainParentFragmentContainer, mainListBuilder.build().fragment)
            .commit()
    }

    override fun goToDetailFragment(newsDataResponse: NewsDataResponse) {
        callback.isEnabled = true
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
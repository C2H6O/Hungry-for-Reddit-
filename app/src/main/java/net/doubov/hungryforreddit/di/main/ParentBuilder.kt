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
import net.doubov.hungryforreddit.di.SingleActivityBuilder
import net.doubov.main.DetailFragment
import net.doubov.main.ListFragment
import net.doubov.main.ParentFragment
import net.doubov.main.routers.ParentRouter
import java.util.*
import javax.inject.Inject
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
    interface ParentFragmentComponent :
        ParentFragmentInjections {

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

@MainParentScope
class ParentFragmentFactory @Inject constructor(
    private val listBuilder: ListBuilder,
    private val detailBuilder: DetailBuilder
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            ListFragment::class.java -> listBuilder.build().fragment
            DetailFragment::class.java -> detailBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}

@MainParentScope
class ParentRouterImpl @Inject constructor(
    val activity: SingleActivity,
    val component: ParentBuilder.ParentFragmentComponent,
    override val fragment: ParentFragment,
    val fragmentFactory: ParentFragmentFactory,
    private val listBuilder: ListBuilder,
    private val detailBuilder: DetailBuilder
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
            .add(R.id.mainParentFragmentContainer, listBuilder.build().fragment)
            .commit()
    }

    override fun goToDetailFragment(newsDataResponse: NewsDataResponse) {
        callback.isEnabled = true
        fragment.childFragmentManager
            .beginTransaction()
            .replace(
                R.id.mainParentFragmentContainer,
                detailBuilder.buildParametrized(newsDataResponse.id, newsDataResponse.title).fragment
            )
            .addToBackStack(null)
            .commit()
    }

}
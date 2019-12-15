package net.doubov.hungryforreddit.ribs

import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.di.SingleActivityComponent
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link RootRibScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class RootBuilder(dependency: SingleActivityComponent) :
    ViewBuilder<RootView, RootRouter, SingleActivityComponent>(dependency) {

    /**
     * Builds a new [RootRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [RootRouter].
     */
    fun build(parentViewGroup: ViewGroup): RootRouter {
        val view = createView(parentViewGroup)
        val interactor = RootInteractor()
        val component = DaggerRootBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.rootRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): RootView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.root_view, parentViewGroup, false) as RootView
    }

    @dagger.Module
    abstract class Module {

        @RootRibScope
        @Binds
        abstract fun presenter(view: RootView): RootInteractor.RootRibPresenter

        @dagger.Module
        companion object {

            @RootRibScope
            @Provides
            @JvmStatic
            fun router(
                component: Component,
                view: RootView,
                interactor: RootInteractor
            ): RootRouter {
                return RootRouter(view, interactor, component)
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @RootRibScope
    @dagger.Component(
        modules = [Module::class],
        dependencies = [SingleActivityComponent::class]
    )
    interface Component : InteractorBaseComponent<RootInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: RootInteractor): Builder

            @BindsInstance
            fun view(view: RootView): Builder

            fun parentComponent(component: SingleActivityComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun rootRouter(): RootRouter
    }

    @Scope
    @Retention(CLASS)
    annotation class RootRibScope

    @Qualifier
    @Retention(CLASS)
    annotation class RootRibInternal
}

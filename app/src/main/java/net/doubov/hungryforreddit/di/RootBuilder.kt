package net.doubov.hungryforreddit.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.hungryforreddit.di.main.ParentBuilder

class RootBuilder(
    private val singleActivity: SingleActivity,
    private val component: SingleActivityComponent
) {

    fun build(): RootRouter {
        val parentBuilder = ParentBuilder(component)
        singleActivity.supportFragmentManager.fragmentFactory =
            RootFragmentFactory(parentBuilder)
        return RootRouter(singleActivity, parentBuilder)
    }

    @ActivityScope
    @Component(
        dependencies = [
            AnotherComponent::class
        ]
    )
    interface SingleActivityComponent :
        SingleActivityInjections {

        fun inject(singleActivity: SingleActivity)

        @Component.Factory
        interface Factory {
            fun create(
                @BindsInstance singleActivity: SingleActivity,
                anotherComponent: AnotherComponent
            ): SingleActivityComponent
        }
    }

    interface SingleActivityInjections : AnotherComponentInjections {
        fun activity(): SingleActivity
    }

}

class RootFragmentFactory constructor(
    private val parentBuilder: ParentBuilder
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            net.doubov.main.ParentFragment::class.java -> parentBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}

class RootRouter(
    private val activity: SingleActivity,
    private val parentBuilder: ParentBuilder
) {

    fun goToMainParentFragment() {
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
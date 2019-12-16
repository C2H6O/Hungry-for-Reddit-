package net.doubov.hungryforreddit.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.hungryforreddit.di.main.parent.MainParentBuilder
import net.doubov.main.MainParentFragment

class RootBuilder(
    private val singleActivity: SingleActivity,
    private val component: SingleActivityComponent
) {

    fun build(): RootRouter {
        val parentBuilder = MainParentBuilder(component)
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

    interface SingleActivityInjections : AnotherComponentInjections

}

class RootFragmentFactory constructor(
    private val mainParentBuilder: MainParentBuilder
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (classLoader.loadClass(className)) {
            MainParentFragment::class.java -> mainParentBuilder.build().fragment
            else -> super.instantiate(classLoader, className)
        }
    }
}

class RootRouter(
    private val activity: SingleActivity,
    private val mainParentBuilder: MainParentBuilder
) {

    fun goToMainParentFragment() {
        activity
            .supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fragmentContainer,
                mainParentBuilder.build().fragment
            )
            .commit()
    }

}
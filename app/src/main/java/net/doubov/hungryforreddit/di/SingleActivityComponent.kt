package net.doubov.hungryforreddit.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import net.doubov.api.RedditApi
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.SingleActivity
import net.doubov.main.MainFragment
import net.doubov.main.MainFragmentModule

@ActivityScope
@Component(
    modules = [
        SingleActivityModule::class,
        MainFragmentModule::class,
        AndroidInjectionModule::class
    ],
    dependencies = [
        AnotherComponent::class
    ]
)
interface SingleActivityComponent : AnotherComponentInjections {

    fun fragmentFactory(): FragmentFactory

    fun inject(singleActivity: SingleActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance singleActivity: SingleActivity,
            singleActivityModule: SingleActivityModule = SingleActivityModule,
            anotherComponent: AnotherComponent
        ): SingleActivityComponent
    }
}

@Module
object SingleActivityModule {
    @ActivityScope
    @Provides
    fun provideFragmentFactory(redditApi: RedditApi): FragmentFactory {
        return object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (className) {
                    MainFragment::class.java.name -> MainFragment(redditApi)
                    else -> super.instantiate(classLoader, className)
                }
            }
        }
    }
}
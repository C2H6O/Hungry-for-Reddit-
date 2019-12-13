package net.doubov.hungryforreddit.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import net.doubov.core.InjectingFragmentFactory
import net.doubov.main.MainParentFragment
import kotlin.reflect.KClass

@Module(includes = [FragmentModule.Bindings::class])
object FragmentModule {

    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(value = AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class FragmentKey(val value: KClass<out Fragment>)

    @Module
    interface Bindings {
        @Binds
        fun provideFragmentFactory(injectingFragmentFactory: InjectingFragmentFactory): FragmentFactory

        @Binds
        @IntoMap
        @FragmentKey(MainParentFragment::class)
        fun provideMainParentFragment(mainParentFragment: MainParentFragment): Fragment
    }
}
package net.doubov.hungryforreddit

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import net.doubov.hungryforreddit.di.AnotherComponent
import net.doubov.hungryforreddit.di.AnotherModule
import net.doubov.hungryforreddit.di.AppComponent
import net.doubov.hungryforreddit.di.AppModule
import net.doubov.hungryforreddit.di.DaggerAnotherComponent
import net.doubov.hungryforreddit.di.DaggerAppComponent
import net.doubov.hungryforreddit.di.YetAnotherModule
import javax.inject.Inject

open class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    protected var _appComponent: AppComponent? = null

    protected var _anotherComponent: AnotherComponent? = null

    override fun onCreate() {
        super.onCreate()

        getAppComponent().inject(this)

        println("LX___ App#onCreate()")
    }

    open fun getAppComponent(): AppComponent {
        if (_appComponent == null) {
            _appComponent = DaggerAppComponent.factory().create(this, AppModule)
        }
        return _appComponent ?: throw IllegalStateException("AppComponent must not be null")
    }

    open fun getAnotherComponent(): AnotherComponent {
        if (_anotherComponent == null) {
            _anotherComponent = DaggerAnotherComponent.factory().create(getAppComponent(), AnotherModule, YetAnotherModule)
        }
        return _anotherComponent ?: throw IllegalStateException("AnotherComponent must not be null")
    }
}
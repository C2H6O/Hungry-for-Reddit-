package net.doubov.hungryforreddit

import android.app.Application
import net.doubov.hungryforreddit.di.AnotherComponent
import net.doubov.hungryforreddit.di.AnotherModule
import net.doubov.hungryforreddit.di.AppComponent
import net.doubov.hungryforreddit.di.AppModule
import net.doubov.hungryforreddit.di.DaggerAnotherComponent
import net.doubov.hungryforreddit.di.DaggerAppComponent
import net.doubov.hungryforreddit.di.YetAnotherModule
import net.doubov.hungryforreddit.di.api.RedditApiModule

open class App : Application() {

    protected var _appComponent: AppComponent? = null

    protected var _anotherComponent: AnotherComponent? = null

    override fun onCreate() {
        super.onCreate()
        println("LX___ App#onCreate()")
    }

    open fun getAppComponent(): AppComponent {
        if (_appComponent == null) {
            _appComponent = DaggerAppComponent.factory().create(this, AppModule, RedditApiModule)
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
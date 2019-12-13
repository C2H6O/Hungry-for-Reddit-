package net.doubov.hungryforreddit

import android.app.Application
import android.content.Context
import net.doubov.hungryforreddit.di.AnotherComponent
import net.doubov.hungryforreddit.di.AppComponent
import net.doubov.hungryforreddit.di.DaggerAnotherComponent
import net.doubov.hungryforreddit.di.DaggerAppComponent

open class App : Application() {

    protected var _appComponent: AppComponent? = null

    protected var _anotherComponent: AnotherComponent? = null

    override fun onCreate() {
        super.onCreate()

        getAppComponent().inject(this)

        println("LX___ App#onCreate()")
    }

    open fun getAppComponent(): AppComponent {
        if (_appComponent == null) {
            _appComponent = DaggerAppComponent.factory().create(this)
        }
        return _appComponent ?: throw IllegalStateException("AppComponent must not be null")
    }

    open fun getAnotherComponent(): AnotherComponent {
        if (_anotherComponent == null) {
            _anotherComponent =
                DaggerAnotherComponent.factory().create(getAppComponent())
        }
        return _anotherComponent ?: throw IllegalStateException("AnotherComponent must not be null")
    }

    companion object {
        fun getApp(context: Context): App {
            return context.applicationContext as App
        }
    }
}
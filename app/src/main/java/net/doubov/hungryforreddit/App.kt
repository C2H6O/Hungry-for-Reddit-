package net.doubov.hungryforreddit

import android.app.Application
import android.content.Context
import net.doubov.core.di.SerializationModule
import net.doubov.hungryforreddit.di.*

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
            _appComponent = DaggerAppComponent.factory().create(this, AppModule, SerializationModule)
        }
        return _appComponent ?: throw IllegalStateException("AppComponent must not be null")
    }

    open fun getAnotherComponent(): AnotherComponent {
        if (_anotherComponent == null) {
            _anotherComponent =
                DaggerAnotherComponent.factory().create(getAppComponent(), AnotherModule, YetAnotherModule)
        }
        return _anotherComponent ?: throw IllegalStateException("AnotherComponent must not be null")
    }

    companion object {
        fun getApp(context: Context): App {
            return context.applicationContext as App
        }
    }
}
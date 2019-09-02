package net.doubov.hungryforreddit

import android.app.Application
import net.doubov.hungryforreddit.di.AnotherComponent
import net.doubov.hungryforreddit.di.AnotherModule
import net.doubov.hungryforreddit.di.AppComponent
import net.doubov.hungryforreddit.di.AppModule
import net.doubov.hungryforreddit.di.DaggerAnotherComponent
import net.doubov.hungryforreddit.di.DaggerAppComponent
import net.doubov.hungryforreddit.di.YetAnotherModule

class App : Application() {

    private lateinit var appComponent: AppComponent

    private lateinit var anotherComponent: AnotherComponent

    override fun onCreate() {
        super.onCreate()
        println("LX___ hello")

        appComponent = DaggerAppComponent.factory().create(this, AppModule)

        anotherComponent = DaggerAnotherComponent.factory().create(appComponent, AnotherModule, YetAnotherModule)
    }
}
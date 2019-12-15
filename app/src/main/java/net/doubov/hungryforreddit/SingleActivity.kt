package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.doubov.hungryforreddit.di.main.DaggerRootBuilder_SingleActivityComponent
import net.doubov.hungryforreddit.di.main.RootBuilder
import net.doubov.hungryforreddit.di.main.RootRouter

class SingleActivity : AppCompatActivity() {

    lateinit var rootBuilder: RootBuilder
    lateinit var rootRouter: RootRouter

    override fun onCreate(savedInstanceState: Bundle?) {

        val component: RootBuilder.SingleActivityComponent = DaggerRootBuilder_SingleActivityComponent
            .factory()
            .create(this, App.getApp(this).getAnotherComponent())

        component.inject(this)

        rootBuilder = RootBuilder(this, component)
        rootRouter = rootBuilder.build()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            rootRouter.goToMainParentFragment()
        }
    }
}
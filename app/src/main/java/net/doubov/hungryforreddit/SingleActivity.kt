package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.doubov.hungryforreddit.di.DaggerRootBuilder_SingleActivityComponent
import net.doubov.hungryforreddit.di.RootBuilder
import net.doubov.hungryforreddit.di.RootRouter

class SingleActivity : AppCompatActivity() {

    private lateinit var rootBuilder: RootBuilder
    private lateinit var rootRouter: RootRouter

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
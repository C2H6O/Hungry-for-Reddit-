package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import net.doubov.hungryforreddit.di.RootBuilder
import net.doubov.hungryforreddit.di.RootRouter
import javax.inject.Inject

class SingleActivity : AppCompatActivity() {

    @Inject
    lateinit var router: RootRouter

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        val rootBuilder = RootBuilder(this, App.getApp(this).getAnotherComponent())
        rootBuilder.build()

        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.goToMainParentFragment()
        }
    }
}
package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import net.doubov.hungryforreddit.di.RootRouter
import net.doubov.hungryforreddit.di.SingleActivityBuilder
import javax.inject.Inject

class SingleActivity : AppCompatActivity() {

    @Inject
    lateinit var router: RootRouter

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        val rootBuilder = SingleActivityBuilder(this, App.getApp(this).getAnotherComponent())
        rootBuilder.build()

        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.goToMainParentFragment()
        }
    }
}
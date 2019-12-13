package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import net.doubov.hungryforreddit.di.DaggerSingleActivityComponent
import net.doubov.hungryforreddit.di.SingleActivityComponent
import net.doubov.main.R
import javax.inject.Inject

class SingleActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    lateinit var component: SingleActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        component = DaggerSingleActivityComponent
            .factory()
            .create(singleActivity = this, anotherComponent = App.getApp(this).getAnotherComponent())
        component.inject(this)

        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragmentContainer, component
                        .mainFragmentComponentFactory()
                        .create()
                        .createMainParentFragmentInstance()
                )
                .commit()
        }
    }
}
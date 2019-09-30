package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import net.doubov.hungryforreddit.di.DaggerSingleActivityComponent
import net.doubov.main.MainFragment
import net.doubov.main.R
import javax.inject.Inject

class SingleActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerSingleActivityComponent
            .factory()
            .create(singleActivity = this, anotherComponent = App.getApp(this).getAnotherComponent())
            .inject(this)

        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragmentFactory.instantiate(classLoader, MainFragment::class.java.name))
                .commit()
        }
    }
}
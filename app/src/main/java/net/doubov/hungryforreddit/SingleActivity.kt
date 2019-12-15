package net.doubov.hungryforreddit

import android.view.ViewGroup
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter
import net.doubov.hungryforreddit.di.DaggerSingleActivityComponent
import net.doubov.hungryforreddit.ribs.RootBuilder

class SingleActivity : RibActivity() {

    override fun createRouter(parentViewGroup: ViewGroup): ViewRouter<*, *, *> {
        return RootBuilder(
            DaggerSingleActivityComponent
                .factory()
                .create(singleActivity = this, anotherComponent = App.getApp(this).getAnotherComponent())
        )
            .build(parentViewGroup)
    }

}
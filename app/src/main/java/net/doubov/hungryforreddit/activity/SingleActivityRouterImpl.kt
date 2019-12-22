package net.doubov.hungryforreddit.activity

import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.di.main.parent.ParentBuilder
import javax.inject.Inject

@ActivityScope
class SingleActivityRouterImpl @Inject constructor(
    private val activity: SingleActivity,
    private val parentBuilder: ParentBuilder
) : SingleActivityRouter {

    override fun goToMainParentFragment() {
        activity
            .supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fragmentContainer,
                parentBuilder.build().fragment
            )
            .commit()
    }
}
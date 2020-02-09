package net.doubov.hungryforreddit.di

import dagger.Component
import net.doubov.core.di.ActivityScope
import net.doubov.hungryforreddit.SingleActivity

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface SingleActivityComponent {

    fun inject(activity: SingleActivity)

}
package net.doubov.hungryforreddit.di.main.parent

import android.content.Context
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import net.doubov.api.models.NewsDataResponse
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.activity.SingleActivity
import net.doubov.hungryforreddit.di.main.detail.DetailBuilder
import net.doubov.hungryforreddit.di.main.list.ListBuilder
import net.doubov.main.DetailFragment
import net.doubov.main.ParentFragment
import net.doubov.main.routers.ParentRouter
import javax.inject.Inject

@MainParentScope
class ParentRouterImpl @Inject constructor(
    activity: SingleActivity,
    override val fragment: ParentFragment,
    val fragmentFactory: ParentFragmentFactory,
    private val listBuilder: ListBuilder,
    private val detailBuilder: DetailBuilder
) : ParentRouter {

    private val callback = activity.onBackPressedDispatcher
        .addCallback(fragment) {
            if (fragment.childFragmentManager.backStackEntryCount > 0) {
                fragment.childFragmentManager.popBackStack()
            }
        }
        .also { it.isEnabled = false }

    init {
        activity
            .supportFragmentManager
            .registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                        if (f == fragment) {
                            fragment.childFragmentManager.fragmentFactory = fragmentFactory
                            fragment.childFragmentManager.registerFragmentLifecycleCallbacks(object :
                                FragmentManager.FragmentLifecycleCallbacks() {
                                override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                                    super.onFragmentAttached(fm, f, context)
                                    when (f::class) {
                                        DetailFragment::class -> callback.isEnabled = true
                                    }
                                }

                                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                                    super.onFragmentDetached(fm, f)
                                    when (f::class) {
                                        DetailFragment::class -> callback.isEnabled = false
                                    }
                                }
                            }, true)
                        }
                    }
                },
                true
            )
    }

    override fun goToListFragment() {
        fragment.childFragmentManager
            .beginTransaction()
            .add(R.id.mainParentFragmentContainer, listBuilder.build().fragment)
            .commit()
    }

    override fun goToDetailFragment(newsDataResponse: NewsDataResponse) {
        callback.isEnabled = true
        fragment.childFragmentManager
            .beginTransaction()
            .replace(
                R.id.mainParentFragmentContainer,
                detailBuilder.buildParametrized(newsDataResponse.id, newsDataResponse.title).fragment
            )
            .addToBackStack(null)
            .commit()
    }

}
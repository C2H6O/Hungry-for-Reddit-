package net.doubov.hungryforreddit.di.main.parent

import androidx.activity.addCallback
import net.doubov.api.models.NewsDataResponse
import net.doubov.core.extensions.onFragmentAttached
import net.doubov.core.extensions.onFragmentDetached
import net.doubov.core.extensions.onFragmentPreAttached
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
    override val fragment: ParentFragment,
    private val activity: SingleActivity,
    private val fragmentFactory: ParentFragmentFactory,
    private val listBuilder: ListBuilder,
    private val detailBuilder: DetailBuilder
) : ParentRouter {

    init {
        setupFragmentFactory()
        setupBackPressHandling()
    }

    private fun setupFragmentFactory() {
        activity.onFragmentPreAttached { f ->
            if (f == fragment) {
                f.childFragmentManager.fragmentFactory = fragmentFactory
            }
        }
    }

    private fun setupBackPressHandling() {
        val callback = activity
            .onBackPressedDispatcher
            .addCallback(fragment) {
                if (fragment.childFragmentManager.backStackEntryCount > 0) {
                    fragment.childFragmentManager.popBackStack()
                }
            }
            .also { it.isEnabled = false }

        activity.onFragmentPreAttached { f ->
            if (f == fragment) {
                fragment.onFragmentAttached { childFragment ->
                    when (childFragment::class) {
                        DetailFragment::class -> callback.isEnabled = true
                    }
                }
                fragment.onFragmentDetached { childFragment ->
                    when (childFragment::class) {
                        DetailFragment::class -> callback.isEnabled = false
                    }
                }
            }
        }
    }

    override fun goToListFragment() {
        fragment.childFragmentManager
            .beginTransaction()
            .add(R.id.mainParentFragmentContainer, listBuilder.build().fragment)
            .commit()
    }

    override fun goToDetailFragment(newsDataResponse: NewsDataResponse) {
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
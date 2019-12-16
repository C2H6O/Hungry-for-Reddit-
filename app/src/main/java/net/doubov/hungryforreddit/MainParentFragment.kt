package net.doubov.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.doubov.hungryforreddit.BaseFragment
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.di.main.parent.MainParentFragmentFactory
import net.doubov.hungryforreddit.di.main.parent.MainParentRouter
import javax.inject.Inject

class MainParentFragment : BaseFragment(R.layout.fragment_main_parent) {

    @Inject
    lateinit var router: MainParentRouter

    @Inject
    lateinit var fragmentFactory: MainParentFragmentFactory

    @Inject
    lateinit var mainListChannel: MainListFragment.MainListChannel

    lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        childFragmentManager.fragmentFactory = fragmentFactory

        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            println("LX___ count: ${childFragmentManager.backStackEntryCount}")
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            }
            onBackPressedCallback.isEnabled = childFragmentManager.backStackEntryCount > 1
        }

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            router.goToListFragment()
        }
    }

    override fun onStart() {
        super.onStart()

        launch {
            for (event in mainListChannel.channel) {
                when (event) {
                    is MainListFragment.Event.OnItemSelected -> router.goToDetailFragment(event.newsDataResponse)
                }
            }
        }

    }

    override fun onStop() {
        coroutineContext[Job]?.cancel()
        super.onStop()
    }
}
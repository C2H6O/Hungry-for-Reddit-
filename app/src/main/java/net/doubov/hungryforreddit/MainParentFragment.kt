package net.doubov.hungryforreddit

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.doubov.hungryforreddit.di.main.MainParentRouter
import javax.inject.Inject

class MainParentFragment : BaseFragment(R.layout.fragment_main_parent) {

    @Inject
    lateinit var router: MainParentRouter

    @Inject
    lateinit var mainListChannel: MainListFragment.MainListChannel

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
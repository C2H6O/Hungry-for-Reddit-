package net.doubov.main

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.launch
import net.doubov.core.ui.BaseFragment
import net.doubov.main.routers.ParentRouter
import javax.inject.Inject

class ParentFragment : BaseFragment(R.layout.fragment_main_parent) {

    @Inject
    lateinit var router: ParentRouter

    @Inject
    lateinit var listChannel: ListFragment.ListChannel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            router.goToListFragment()
        }

        launch {
            for (event in listChannel.channel) {
                when (event) {
                    is ListFragment.Event.OnItemSelected -> router.goToDetailFragment(event.newsDataResponse)
                }
            }
        }
    }
}
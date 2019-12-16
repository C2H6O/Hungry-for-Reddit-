package net.doubov.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.di.main.parent.MainParentFragmentFactory
import net.doubov.hungryforreddit.di.main.parent.MainParentRouter
import javax.inject.Inject

class MainParentFragment : Fragment(R.layout.fragment_main_parent), CoroutineScope by MainScope() {

    @Inject
    lateinit var router: MainParentRouter

    @Inject
    lateinit var fragmentFactory: MainParentFragmentFactory

    @Inject
    lateinit var mainListChannel: MainListFragment.MainListChannel

    override fun onAttach(context: Context) {
        childFragmentManager.fragmentFactory = fragmentFactory
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

        println("LX___ mainListChannel: $mainListChannel")

        launch {
            for (event in mainListChannel.channel) {
                println("LX___ event!! $event")
            }
        }

    }

    override fun onStop() {
        coroutineContext[Job]?.cancel()
        super.onStop()
    }
}
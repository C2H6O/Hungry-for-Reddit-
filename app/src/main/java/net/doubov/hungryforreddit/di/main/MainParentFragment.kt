package net.doubov.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.di.main.parent.MainParentFragmentFactory
import net.doubov.hungryforreddit.di.main.parent.MainParentRouter
import javax.inject.Inject

class MainParentFragment : Fragment(R.layout.fragment_main_parent) {

    @Inject
    lateinit var router: MainParentRouter

    @Inject
    lateinit var fragmentFactory: MainParentFragmentFactory

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
}